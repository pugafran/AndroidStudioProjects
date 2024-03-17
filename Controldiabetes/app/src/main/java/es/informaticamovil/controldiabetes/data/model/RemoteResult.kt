package es.informaticamovil.controldiabetes.data.model

data class RemoteResult(
    val code: String,
    val product: Product,
    val status: Int,
    val status_verbose: String
)