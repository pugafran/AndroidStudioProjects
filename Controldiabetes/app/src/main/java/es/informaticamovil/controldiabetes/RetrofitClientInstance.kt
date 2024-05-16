package es.informaticamovil.controldiabetes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import es.informaticamovil.controldiabetes.ApiService

object RetrofitClientInstance {
    private const val BASE_URL = "https://world.openfoodfacts.org/api/v2/"

    fun getService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
