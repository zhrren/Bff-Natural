package app.config

import app.base.cutil.JsonUtil
import app.base.cutil.Meta
import io.vertx.pgclient.PgException
import mu.KotlinLogging
import java.sql.SQLException

/**
 *
 * Created by zhong on 2022/7/23
 */
object FailureParser {
  private fun Throwable.toMeta(): Meta {
    val error = this as? Meta
    if (error != null) {
      return error
    }

    val name = javaClass.simpleName

    val pgException = this as? PgException
    if (pgException != null) {
      return Meta(name, pgException.errorMessage ?: "", null)
    }

    val message = if (message != null) message.orEmpty() else toString()
    return Meta(name, message, null)
  }

  private fun Throwable.info(): String {
    if (this is Meta) {
      return JsonUtil.toString(this)
    }
    return stackTraceToString()
  }

  data class Failure(val statusCode: Int, val response: Meta)

  private val logger = KotlinLogging.logger { }

  fun parse(statusCode: Int, error: Throwable): Failure {
    return when (error) {
      is SQLException -> Failure(statusCode, Meta.failure(error.javaClass.name, "执行错误"))
      else -> Failure(statusCode, error.toMeta())
    }
  }
}
