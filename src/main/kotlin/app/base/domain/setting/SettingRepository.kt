package app.base.domain.setting

import app.base.cutil.DbRepository

interface SettingRepository : DbRepository<String, Setting> {
  suspend fun find(key: String): List<Setting>
  suspend fun getItem(key: String): Setting?
  suspend fun setItem(key: String, value: String)
  suspend fun removeItem(key: String)
}
