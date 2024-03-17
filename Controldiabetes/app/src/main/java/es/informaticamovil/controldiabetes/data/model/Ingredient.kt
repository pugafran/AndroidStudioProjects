package es.informaticamovil.controldiabetes.data.model

data class Ingredient(
    val id: String,
    val ingredients: List<IngredientX>,
    val percent_estimate: Int,
    val percent_max: Int,
    val percent_min: Int,
    val text: String,
    val vegan: String,
    val vegetarian: String
)