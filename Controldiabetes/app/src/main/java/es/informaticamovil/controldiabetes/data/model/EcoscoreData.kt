package es.informaticamovil.controldiabetes.data.model

data class EcoscoreData(
    val adjustments: Adjustments,
    val agribalyse: Agribalyse,
    val missing: Missing,
    val missing_agribalyse_match_warning: Int,
    val scores: Scores,
    val status: String
)