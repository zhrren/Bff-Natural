package app.config

import app.base.cutil.JsonUtil
import app.base.cutil.Transformer
import app.base.kernel.Settings

/**
 *
 * Created by zhong on 2022/8/15
 */
object StartupConfig {
  fun configure() {
  }

  fun configure(settings: Settings) {
    TransformerConfig.configure()
    JsonUtil.configure(settings.debug, Transformer.transformers)
  }
}
