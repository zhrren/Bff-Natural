package app.config

import app.base.cutil.Transformer
import app.base.transformer.AddressTransformer

object TransformerConfig {
  fun configure() {
    Transformer.register(AddressTransformer())
  }
}
