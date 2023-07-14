package hvalfangst.kafka.utils

import com.fasterxml.jackson.databind.ObjectMapper

class JSONHelpers {
    companion object {

        inline fun <reified T> parseJson(jsonString: String): T? {
            return try {
                val objectMapper = ObjectMapper()
                objectMapper.readValue(jsonString, T::class.java)
            } catch (e: Exception) {
                null
            }
        }

        inline fun <reified T> convertToJson(request: T): String {
            val objectMapper = ObjectMapper()
            return objectMapper.writeValueAsString(request)
        }

    }
}
