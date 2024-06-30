package es.informaticamovil.controldiabetes.data.data

import Product

data class ProductResponse(
    val code: String,
    val product: Product,
    val status: Int,
    val status_verbose: String
)
