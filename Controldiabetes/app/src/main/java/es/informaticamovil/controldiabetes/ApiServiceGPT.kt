package es.informaticamovil.controldiabetes

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class ChatGPTRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

data class ChatGPTResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

interface ApiServiceGPT {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    fun getChatResponse(
        @Body request: ChatGPTRequest
    ): Call<ChatGPTResponse>
}
