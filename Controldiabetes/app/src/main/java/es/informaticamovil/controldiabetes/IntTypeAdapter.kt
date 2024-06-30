package es.informaticamovil.controldiabetes

import com.google.gson.*
import java.lang.reflect.Type

class IntTypeAdapter : JsonDeserializer<Int?>, JsonSerializer<Int?> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Int? {
        return try {
            if (json.asString.isEmpty()) {
                0 // valor por defecto para cadenas vacías
            } else {
                json.asInt
            }
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }

    override fun serialize(src: Int?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src)
    }
}
