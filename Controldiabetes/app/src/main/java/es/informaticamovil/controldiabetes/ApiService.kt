package es.informaticamovil.controldiabetes

import Product
import es.informaticamovil.controldiabetes.data.data.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path



interface ApiService {


    @GET("product/{barcode}.json")
    suspend fun getResponse(@Path("barcode") barcode: String): ProductResponse
}
