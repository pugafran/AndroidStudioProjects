package es.imovil.recyclerviewprac

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class MiAdapter: BaseAdapter() {
    override fun getCount(): Int {
        TODO("Devuele el número de items del elemento")
    }

    override fun getItem(position: Int): Any {
        TODO("Devuelve el item en la posición solicitada")
    }

    override fun getItemId(position: Int): Long {
        TODO("Devuelve el Id de un item")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Implementa el layout de cada item completado con los datos")
    }
}