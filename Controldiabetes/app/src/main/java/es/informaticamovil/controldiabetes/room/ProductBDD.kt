package es.informaticamovil.controldiabetes.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class ProductBDD (
    @PrimaryKey(autoGenerate = true)
    val Id: Int,
    val nombre: String,
    val carbohidratos: Double,
    val marca: String,
    val nova: Int,
    val ecoscore_grade: String,
    val analisis: List<String>,
    val fecha: String
)