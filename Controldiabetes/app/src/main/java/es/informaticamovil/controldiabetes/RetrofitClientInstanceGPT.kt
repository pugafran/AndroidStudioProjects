package es.informaticamovil.controldiabetes

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstanceGPT {

    private const val BASE_URL = "https://api.openai.com/"

    val gson = GsonBuilder().create()

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer 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")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiServiceGPT by lazy {
        retrofit.create(ApiServiceGPT::class.java)
    }

    fun getService(): ApiServiceGPT {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServiceGPT::class.java)
    }
}
