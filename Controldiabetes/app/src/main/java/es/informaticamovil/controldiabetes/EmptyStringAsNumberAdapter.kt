package es.informaticamovil.controldiabetes

import com.google.gson.*
import java.lang.reflect.Type

class EmptyStringAsNumberAdapter : JsonDeserializer<Number?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Number? {
        return try {
            if (json.asString.isEmpty()) {
                0 // valor por defecto
            } else {
                json.asNumber
            }
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }
}
