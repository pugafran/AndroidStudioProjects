package es.informaticamovil.controldiabetes.data.model

data class DataX(
    val components: Components,
    val count_proteins: Int,
    val count_proteins_reason: String,
    val is_beverage: Int,
    val is_cheese: Int,
    val is_fat_oil_nuts_seeds: Int,
    val is_red_meat_product: Int,
    val is_water: Int,
    val negative_points: Int,
    val negative_points_max: Int,
    val positive_nutrients: List<String>,
    val positive_points: Int,
    val positive_points_max: Int
)