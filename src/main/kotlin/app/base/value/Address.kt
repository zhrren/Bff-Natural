package app.base.value

import app.base.cutil.JsonUtil
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

data class AddressItem(val code: Long, val name: String)

data class Address(
  var items: List<AddressItem> = listOf(),
  val detail: String = "",
  val lng: Double = 0.0,
  val lat: Double = 0.0
) {
  val province: Long
    get() = items.getOrNull(0)?.code ?: 0

  val city: Long
    get() = items.getOrNull(1)?.code ?: 0

  val district: Long
    get() = items.getOrNull(2)?.code ?: 0

  val formatted: String
    get() = items.joinToString { it.name } + detail

  companion object {
    fun parse(address: String?): Address? {
      if (address.isNullOrBlank()) return null
      return JsonUtil.parseObject(address, Address::class.java)
    }
  }
}

class AddressSerializer : JsonSerializer<Address>() {
  override fun serialize(value: Address, gen: JsonGenerator, serializers: SerializerProvider) {
    gen.writeString(JsonUtil.toString(value))
  }
}

class AddressDeserializer : JsonDeserializer<Address>() {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Address {
    return JsonUtil.parseObject(p.text, Address::class.java)
  }
}
