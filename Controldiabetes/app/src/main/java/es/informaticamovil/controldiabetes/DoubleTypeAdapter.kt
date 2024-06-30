package es.informaticamovil.controldiabetes

import com.google.gson.*
import java.lang.reflect.Type

class DoubleTypeAdapter : JsonDeserializer<Double?>, JsonSerializer<Double?> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Double? {
        return try {
            if (json.asString.isEmpty()) {
                0.0 // Valor por defecto para cadenas vacías
            } else {
                json.asDouble
            }
        } catch (e: NumberFormatException) {
            throw JsonSyntaxException(e)
        }
    }

    override fun serialize(src: Double?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src)
    }
}
