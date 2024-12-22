package app.infra.db

import app.base.cutil.*
import app.base.cutil.LangUtil.defaultAsNull
import app.base.domain.setting.Setting
import app.base.domain.setting.SettingRepository
import com.google.inject.Inject
import com.google.inject.Singleton

@Singleton
class SettingRepositoryImpl private constructor(rcp: DbRcp) :
  SettingRepository,
  DbRepositoryImpl<String, Setting>(rcp, Setting::class.java) {
  @Inject
  constructor(dbPool: DbPool) : this(DbRcp.of(dbPool))
  constructor(dbClient: DbClient) : this(DbRcp.of(dbClient))

  override suspend fun find(key: String): List<Setting> {
    val builder = DbSelectBuilder()
      .from("setting")
      .whereAnd("key=#{key}", mapOf("key" to key), key.defaultAsNull() != null)
    return useClient { dbClient ->
      dbClient.find(builder.generate(), builder.parameters(), Setting::class.java)
    }
  }

  override suspend fun getItem(key: String): Setting? {
    if (key.isBlank()) return null
    return useClient { dbClient ->
      dbClient.get(mapOf("key" to key), Setting::class.java)
    }
  }

  override suspend fun setItem(key: String, value: String) {
    return useClient { client ->
      client.execute(
        "update setting set value=#{value} where key=#{key}",
        mapOf("value" to value, "key" to key)
      )
    }
  }

  override suspend fun removeItem(key: String) {
    return useClient { client ->
      client.execute(
        "delete setting where key=#{key}",
        mapOf("key" to key)
      )
    }
  }
}
