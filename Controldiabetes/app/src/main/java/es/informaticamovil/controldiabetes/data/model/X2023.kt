package es.informaticamovil.controldiabetes.data.model

data class X2023(
    val category_available: Int,
    val `data`: DataX,
    val grade: String,
    val nutrients_available: Int,
    val nutriscore_applicable: Int,
    val nutriscore_computed: Int,
    val score: Int
)