package app.config

import app.base.cutil.*
import app.base.kernel.Settings
import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import io.vertx.core.Vertx
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object InjectConfig {
  fun configure(
    vertx: Vertx,
    settings: Settings
  ): Injector {
    return Guice.createInjector(InjectorModule(vertx, settings))
  }
}

class InjectorModule(
  private val vertx: Vertx,
  private val settings: Settings,
) : AbstractModule() {
  override fun configure() {
//    val dbPool = getDbPool(vertx, settings)

    bind(Vertx::class.java).toInstance(vertx)
    bind(CoroutineScope::class.java).toInstance(CoroutineScope(Dispatchers.IO))
    bind(Settings::class.java).toInstance(settings)
//    bind(DbPool::class.java).toInstance(dbPool)

    bind(IdGenerator::class.java).to(IdGeneratorSnowflakeId53Impl::class.java)
  }
}

private fun getDbPool(vertx: Vertx, settings: Settings): DbPool {
  val connectOptions = PgConnectOptions().setPort(settings.db_port).setHost(settings.db_host)
    .setDatabase(settings.db_name).setUser(settings.db_username).setPassword(
      settings.db_password
    )
    .setTcpKeepAlive(true)
  val poolOptions = PoolOptions().setMaxSize(10)
  val pool = PgPool.pool(vertx, connectOptions, poolOptions)
  return DbPoolImpl(DbType.PostgreSQL, pool)
}
