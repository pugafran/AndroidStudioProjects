package es.informaticamovil.controldiabetes

import androidx.lifecycle.ViewModel
import es.informaticamovil.controldiabetes.room.ProductBDD

class HistoryVM : ViewModel() {
        var nombre: String = ""
        var carbohidratos: Double = 0.0
        var fecha: String = ""
        var historyList: List<ProductBDD> = emptyList()
        var filtered: Boolean = false

}
