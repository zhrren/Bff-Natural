package app.infra.db

import app.base.cutil.*
import app.base.domain.setting.SettingRepository
import app.base.kernel.Settings
import app.base.port.db.DbContext
import app.base.port.db.DbContextFactory
import app.base.port.db.DbContextTransaction
import com.fasterxml.jackson.core.type.TypeReference
import com.google.inject.Inject
import com.google.inject.Singleton
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

@Singleton
class DbContextFactoryImpl @Inject constructor(
  private val dbPool: DbPool,
  private val settings: Settings
) :
  DbContextFactory {

  private val statement = DbStatement(dbPool.dbType)

  override suspend fun createContext(): DbContext {
    return DbContextImpl(dbPool.getClient(), settings)
  }

  override suspend fun createTransaction(): DbContextTransaction {
    val transaction = dbPool.beginTransaction()
    return DbContextTransactionImpl(transaction, settings)
  }

  override suspend fun <T> parseObject(value: JsonObject, clazz: Class<T>): T {
    return JsonUtil.parseObject(value, clazz, statement.snakeCase())
  }

  override suspend fun <T> parseArray(value: JsonArray, typeReference: TypeReference<List<T>>): List<T> {
    return JsonUtil.parseList(value, typeReference, statement.snakeCase())
  }
}

class DbContextTransactionImpl(
  private val dbTransaction: DbTransaction,
  private val settings: Settings
) :
  DbContextImpl(dbTransaction, settings),
  DbContextTransaction {

  override suspend fun commit() {
    dbTransaction.commit()
  }

  override suspend fun rollback() {
    dbTransaction.rollback()
  }
}

open class DbContextImpl(
  private val dbClient: DbClient,
  private val settings: Settings
) : DbContext {
  override fun close() {
    dbClient.close()
  }

  override val setting: SettingRepository = SettingRepositoryImpl(dbClient)
}
