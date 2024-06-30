package es.informaticamovil.controldiabetes

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {
    private const val BASE_URL = "https://world.openfoodfacts.org/api/v2/"

    val gson = GsonBuilder()
        .registerTypeAdapter(Int::class.java, EmptyStringAsNumberAdapter())
        .registerTypeAdapter(Long::class.java, EmptyStringAsNumberAdapter())
        .registerTypeAdapter(Double::class.java, EmptyStringAsNumberAdapter())
        .registerTypeAdapter(Int::class.java, IntTypeAdapter())
        .registerTypeAdapter(Double::class.java, DoubleTypeAdapter())
        
        .create()

    fun getService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}
