package app.base.port.db

import app.base.cutil.DbTransactional
import app.base.domain.setting.SettingRepository
import com.fasterxml.jackson.core.type.TypeReference
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.io.Closeable

interface DbContextFactory {
  suspend fun createContext(): DbContext

  suspend fun createTransaction(): DbContextTransaction

  suspend fun <T> parseObject(value: JsonObject, clazz: Class<T>): T

  suspend fun <T> parseArray(value: JsonArray, typeReference: TypeReference<List<T>>): List<T>
}

interface DbContextTransaction : DbContext, DbTransactional

interface DbContext : Closeable {
  val setting: SettingRepository
}
