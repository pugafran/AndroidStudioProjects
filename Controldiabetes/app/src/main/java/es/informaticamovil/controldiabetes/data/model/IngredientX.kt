package es.informaticamovil.controldiabetes.data.model

data class IngredientX(
    val ciqual_food_code: String,
    val ciqual_proxy_food_code: String,
    val from_palm_oil: String,
    val id: String,
    val ingredients: List<IngredientXX>,
    val percent_estimate: Double,
    val percent_max: Double,
    val percent_min: Int,
    val text: String,
    val vegan: String,
    val vegetarian: String
)