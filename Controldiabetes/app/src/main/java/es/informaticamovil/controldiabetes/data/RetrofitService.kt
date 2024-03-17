package es.informaticamovil.controldiabetes.data

import es.informaticamovil.controldiabetes.data.model.RemoteResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class RetrofitService {


    @GET("{barcode}.json")
    suspend fun getProducto(@Query ("barcode") barcode: String): RemoteResult {
        return RetrofitServiceFactory.makeRetrofitService().getProducto(barcode)
    }

    object RetrofitServiceFactory {
        fun makeRetrofitService(): RetrofitService {
            return Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/api/v2/product/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitService::class.java)
        }
    }
}