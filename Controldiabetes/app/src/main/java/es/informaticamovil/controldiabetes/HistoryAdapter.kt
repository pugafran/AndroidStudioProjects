package es.informaticamovil.controldiabetes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.informaticamovil.controldiabetes.room.ProductBDD
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private var historyList: List<ProductBDD>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    fun updateHistory(newHistoryList: List<ProductBDD>) {
        historyList = newHistoryList
        notifyDataSetChanged()
    }

    fun getNovaContext(novaGroup: Int): String {
        return when (novaGroup) {
            1 -> "1 - Alimentos no procesados o mínimamente"
            2 -> "2 - Ingredientes culinarios procesados"
            3 -> "3 - Alimentos procesados"
            4 -> "4 - Alimentos y bebidas ultraprocesados"
            else -> "No clasificado"
        }
    }

    fun getEcoscoreContext(grade: String): String {
        return when (grade) {
            "a" -> "A - Muy bueno"
            "b" -> "B - Bueno"
            "c" -> "C - Regular"
            "d" -> "D - Malo"
            "e" -> "E - Muy malo"
            else -> "No clasificado"
        }
    }

    fun getAnalysisTags(tags: List<String>): String {
        var analysisTags = ""

        if (tags.contains("en:palm-oil-free")) {
            analysisTags += "Sin aceite de palma, "
        } else if (tags.contains("en:palm-oil")) {
            analysisTags += "Con aceite de palma, "
        } else {
            analysisTags += "Sin información de si contiene aceite de palma, "
        }

        if (tags.contains("en:vegan")) {
            analysisTags += "vegano, "
        } else if (tags.contains("en:non-vegan")) {
            analysisTags += "no vegano, "
        } else {
            analysisTags += "sin información de si es vegano, "
        }

        if (tags.contains("en:vegetarian")) {
            analysisTags += "vegetariano."
        } else if (tags.contains("en:non-vegetarian")) {
            analysisTags += "no vegetariano."
        } else {
            analysisTags += "sin información de si es vegetariano."
        }

        return analysisTags
    }

    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreTextView2)
        val carbohidratosTextView: TextView = view.findViewById(R.id.carbohidratosTextView)
        val marcaTextView: TextView = view.findViewById(R.id.marcaTextView)
        val novaTextView: TextView = view.findViewById(R.id.novaTextView)
        val ecoscoreTextView: TextView = view.findViewById(R.id.ecoscoreTextView)
        val analisisTextView: TextView = view.findViewById(R.id.analisisTextView)
        val fechaTextView: TextView = view.findViewById(R.id.fechaTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = historyList[position]
        holder.nombreTextView.text = "Nombre: ${product.nombre}"
        holder.carbohidratosTextView.text = "Carbohidratos: ${product.carbohidratos}"
        holder.marcaTextView.text = "Marca: ${product.marca}"
        holder.novaTextView.text = "NOVA: ${getNovaContext(product.nova)}"
        holder.ecoscoreTextView.text = "Ecoscore: ${getEcoscoreContext(product.ecoscore_grade)}"
        holder.analisisTextView.text = "Análisis: ${getAnalysisTags(product.analisis)}"
        holder.fechaTextView.text = "Fecha: ${formatDate(product.fecha)}"
    }

    override fun getItemCount() = historyList.size
}
