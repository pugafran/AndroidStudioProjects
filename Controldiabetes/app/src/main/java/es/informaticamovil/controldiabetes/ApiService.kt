package es.informaticamovil.controldiabetes

import Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("product/{barcode}.json")
    suspend fun getProducto(@Path("barcode") barcode: String): Product
}
