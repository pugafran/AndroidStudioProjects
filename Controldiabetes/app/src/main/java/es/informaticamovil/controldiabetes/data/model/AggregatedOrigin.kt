package es.informaticamovil.controldiabetes.data.model

data class AggregatedOrigin(
    val epi_score: String,
    val origin: String,
    val percent: Int,
    val transportation_score: Any
)