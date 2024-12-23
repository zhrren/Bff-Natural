package app.verticle

import com.google.inject.Inject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import mu.KotlinLogging

class MainVerticle @Inject constructor(
  private val webVerticle: WebVerticle,
) : CoroutineVerticle() {
  private val logger = KotlinLogging.logger { }

  override suspend fun start() {
    val verticles = listOf(
      webVerticle,
    )

    for (verticle in verticles) {
      vertx.deployVerticle(verticle).onComplete {
        val simpleName = verticle.javaClass.simpleName
        if (it.failed()) {
          logger.error { "$simpleName startup failed: ${it.cause()?.stackTraceToString()}" }
        } else {
          logger.info { "$simpleName startup successfully" }
        }
      }
    }
  }
}
