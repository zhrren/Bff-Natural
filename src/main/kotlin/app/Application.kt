package app

import app.base.cutil.Configuration
import app.base.kernel.Settings
import app.config.InjectConfig
import app.config.StartupConfig
import app.verticle.MainVerticle
import io.vertx.core.Vertx
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

object Application {
  private val logger = KotlinLogging.logger { }

  @JvmStatic
  fun main(args: Array<String>) {
    StartupConfig.configure()

    runBlocking {
      val vertx = Vertx.vertx()
      val settings = Settings(Configuration.load())
      StartupConfig.configure(settings)
      val getIt = InjectConfig.configure(vertx, settings)

      val mainVerticle = getIt.getInstance(MainVerticle::class.java)
      vertx.deployVerticle(mainVerticle).onComplete {
        if (it.failed()) {
          logger.error { "MainVerticle startup failed: ${it.cause()?.stackTraceToString()}" }
        } else {
          logger.info { "MainVerticle startup successfully" }
        }
      }
    }
  }
}
