package es.informaticamovil.controldiabetes.data.model

data class Negative(
    val id: String,
    val points: Int,
    val points_max: Int,
    val unit: String,
    val value: Double
)