package app.verticle

import app.base.cutil.ApidocBuilder
import app.base.cutil.JsonUtil
import app.base.cutil.Meta
import app.base.cutil.RestBuilder
import app.base.kernel.Settings
import app.case.auth.AuthenticationService
import app.config.FailureParser
import com.google.inject.Inject
import com.google.inject.Injector
import io.vertx.core.Handler
import io.vertx.core.cli.UsageMessageFormatter
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.AuthenticationHandler
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging

class WebVerticle @Inject constructor(
  private val getIt: Injector,
  private val settings: Settings,
  private val authenticationService: AuthenticationService,
) : CoroutineVerticle() {
  private val logger = KotlinLogging.logger { }
  private val coroutineScope = CoroutineScope(Dispatchers.IO)

  override suspend fun start() {
    val rootRouter = Router.router(vertx)
    val router = Router.router(vertx)
    setupRouter(rootRouter, router)

    val options = HttpServerOptions().setMaxFormAttributeSize(1024 * 1024)
    val server = vertx.createHttpServer(options)
      .requestHandler(rootRouter)
      .listen(settings.api_http_port)
      .await()
    logger.info { "http server start - http://127.0.0.1:${server.actualPort()}" }
  }

  override suspend fun stop() {
  }

  private suspend fun setupRouter(rootRouter: Router, router: Router) {
    rootRouter.mountSubRouter(settings.api_mount_point, router)
    router.route()
      .handler(corsHandler)
      .failureHandler(errorHandler)
      .handler(BodyHandler.create())

    val authHandler =
      TokenAuthenticationHandler(coroutineScope, settings, authenticationService)
    router.route("/*").handler(authHandler)

    val restBuilder = RestBuilder(router).build { service ->
      getIt.getInstance(
        service
      )
    }
    authHandler.anonymous.addAll(restBuilder.anonymousPaths)

    try {
      ApidocBuilder(router, settings.api_http_port, settings.api_apidoc_token).build()
    } catch (ex: Throwable) {
      logger.error { "ApidocBuilder build error, ${ex.stackTraceToString()}" }
    }
  }

  private val corsHandler = CorsHandler.create("*")
    .allowedMethod(HttpMethod.GET)
    .allowedMethod(HttpMethod.POST)
    .allowedMethod(HttpMethod.PUT)
    .allowedMethod(HttpMethod.DELETE)
    .allowedMethod(HttpMethod.OPTIONS)

  private val errorHandler = Handler<RoutingContext> {
    logger.error { "${it.request().uri()}: ${it.failure().stackTraceToString()}" }

    if (it.failure() == null) {
      val res = it.response()
      res.putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      res.statusCode = it.statusCode()
      res.end()
    } else {
      val failure = FailureParser.parse(it.statusCode(), it.failure())
      val res = it.response()
      res.putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      res.statusCode = failure.statusCode
      res.end(JsonUtil.toString(failure.response))
    }
  }

  class TokenAuthenticationHandler(
    val scope: CoroutineScope,
    val settings: Settings,
    val authenticationService: AuthenticationService
  ) : AuthenticationHandler {
    override fun handle(event: RoutingContext) {
      val logger = KotlinLogging.logger { }
      val path = event.request().path().replace("${settings.api_mount_point}/", "/")
      if (anonymous.contains(path)) {
        event.next()
        return
      }
      val authorization = event.request().getHeader("Authorization")
      if (UsageMessageFormatter.isNullOrEmpty(authorization) || !authorization.startsWith("token ")) {
        event.fail(401, Meta.unauthorized("token"))
        return
      }

      val token = authorization.substring(6)
      scope.launch {
        val authenticationUser = authenticationService.handle(event)
        if (authenticationUser != null) {
          event.setUser(authenticationUser)
          event.next()
        } else {
          event.fail(401, Meta.unauthorized("token"))
        }
      }
    }

    var anonymous = mutableListOf<String>(
      "/apidoc.json"
    )
  }
}
