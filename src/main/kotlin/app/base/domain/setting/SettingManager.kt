package app.base.domain.setting

import app.base.cutil.IdGenerator
import cn.hutool.extra.cglib.CglibUtil
import com.google.inject.Inject
import com.google.inject.Singleton
import mu.KotlinLogging

@Singleton
class SettingManager @Inject constructor(
  private val idGenerator: IdGenerator,
  private val settingRepository: SettingRepository
) {
  private val logger = KotlinLogging.logger { }

  suspend fun save(key: String, value: String): Setting {
    return when (val isExists = settingRepository.getItem(key)) {
      null -> {
        val setting = Setting()
        setting.id = idGenerator.next()
        setting.key = key
        setting.value = value
        settingRepository.create(setting)
        setting
      }

      else -> {
        val setting = CglibUtil.copy(isExists, Setting::class.java)
        setting.value = value
        settingRepository.update(setting)
        setting
      }
    }
  }
}
