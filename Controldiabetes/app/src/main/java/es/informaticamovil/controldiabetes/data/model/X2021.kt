package es.informaticamovil.controldiabetes.data.model

data class X2021(
    val category_available: Int,
    val `data`: Data,
    val grade: String,
    val nutrients_available: Int,
    val nutriscore_applicable: Int,
    val nutriscore_computed: Int,
    val score: Int
)