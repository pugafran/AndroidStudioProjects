package es.informaticamovil.controldiabetes.data.model

data class IngredientXX(
    val ciqual_food_code: String,
    val id: String,
    val percent_estimate: Double,
    val percent_max: Double,
    val percent_min: Int,
    val text: String,
    val vegan: String,
    val vegetarian: String
)