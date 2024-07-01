package es.informaticamovil.controldiabetes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.informaticamovil.controldiabetes.databinding.ActivityHistoryBinding
import es.informaticamovil.controldiabetes.room.ProductApp
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    val app by lazy { application as ProductApp }

    private lateinit var adapter: HistoryAdapter

    val VM: HistoryVM by lazy {
        ViewModelProvider(this).get(HistoryVM::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        // Restablecer los filtros y la lista de historial del ViewModel
        binding.filterName.setText(VM.nombre)
        binding.filterCarbohydrates.setText(if (VM.carbohidratos != 0.0) VM.carbohidratos.toString() else "")
        binding.filterDate.setText(VM.fecha)

        if (VM.filtered) {
            adapter.updateHistory(VM.historyList)
        } else {
            loadHistory()
        }

        binding.buttonFilter.setOnClickListener {
            VM.nombre = binding.filterName.text.toString()
            VM.carbohidratos = binding.filterCarbohydrates.text.toString().toDoubleOrNull() ?: 0.0
            VM.fecha = binding.filterDate.text.toString()

            filterHistory(VM.nombre, VM.carbohidratos, invertirFecha(VM.fecha))
        }
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            val history = app.room.productDao().getAll()
            VM.historyList = history
            adapter.updateHistory(history)
        }
    }

    private fun filterHistory(name: String, carbohydrates: Double, date: String) {
        lifecycleScope.launch {
            val filteredHistory = app.room.productDao().filterProducts(
                if (name.isNotEmpty()) "%$name%" else null,
                if (carbohydrates != 0.0) carbohydrates else null,
                if (date.isNotEmpty()) date else null
            )
            VM.historyList = filteredHistory
            VM.filtered = true
            adapter.updateHistory(filteredHistory)
        }
    }

    private fun invertirFecha(fecha: String): String {
        val partes = fecha.split("-")
        return if (partes.size == 3) {
            "${partes[2]}-${partes[1]}-${partes[0]}"
        } else {
            fecha
        }
    }
}
