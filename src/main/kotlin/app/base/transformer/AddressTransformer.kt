package app.base.transformer

import app.base.cutil.Transformer
import app.base.value.Address

class AddressTransformer : Transformer<Address, Map<*, *>> {
  override fun serialize(original: Address?): Map<*, *>? {
    return mapper.readValue(mapper.writeValueAsString(original), Map::class.java)
  }

  override fun deserialize(transformed: Any?): Address? {
    if (transformed is String) {
      return mapper.readValue(transformed as String, Address::class.java)
    }
    if (transformed is Map<*, *>) {
      return mapper.readValue(mapper.writeValueAsString(transformed), Address::class.java)
    }
    return null
  }
}
