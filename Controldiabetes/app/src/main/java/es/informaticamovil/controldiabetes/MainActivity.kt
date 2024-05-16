package es.informaticamovil.controldiabetes

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import es.informaticamovil.controldiabetes.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {





    private val service by lazy { RetrofitClientInstance.getService() }



    val bindingo: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater);
    }

    val VM: AlimentosVM by lazy {
        ViewModelProvider(this).get(AlimentosVM::class.java);
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingo.root);
        val nombresAlimentos = VM.alimentos.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresAlimentos)
        bindingo.autoCompleteTextViewAlimento.setAdapter(adapter);
        bindingo.autoCompleteTextViewAlimento.threshold = 1; // Empieza a mostrar sugerencias después de 1 carácter.


        bindingo.buttonBarras.setOnClickListener(this);
        bindingo.buttonCalcular.setOnClickListener(this);
        bindingo.buttonLimpiar.setOnClickListener(this);










    }



    fun calcularRaciones(alimento: String, gramos: String) {
        val gramosNumericos = gramos.toDoubleOrNull()
        if (gramosNumericos == null || gramosNumericos < 0) {
            // Mostrar un mensaje de error al usuario
            return
        }

        val alimentoEncontrado = VM.alimentos.find { it.nombre == alimento }
        if (alimentoEncontrado == null) {
            // Mostrar un mensaje de error al usuario
            return
        }

        val carbohidratos = (alimentoEncontrado.carbohidratos * gramosNumericos) / 100
        val indiceGlucemico = alimentoEncontrado.indiceGlucemico

        // Actualizar UI con estos valores...




        VM.totalCarbohidratos += carbohidratos
        VM.totalIndiceGlucemicoPonderado += indiceGlucemico * carbohidratos
        VM.totalCarbohidratosPonderados += carbohidratos

        val indiceGlucemicoMedio = if (VM.totalCarbohidratos == 0.0) 0.0 else VM.totalIndiceGlucemicoPonderado / VM.totalCarbohidratosPonderados

        bindingo.labelCarbohidratos.text = "Carbohidratos totales: ${"%.2f".format(VM.totalCarbohidratos)}";
        bindingo.labelIndiceGlucemico.text = "Índice glucémico medio: ${indiceGlucemicoMedio}";

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            bindingo.buttonBarras.id -> {
                // Asumiendo que ya configuraste las opciones de tu escáner.
                val scanner = GmsBarcodeScanning.getClient(this, GmsBarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                    .enableAutoZoom()
                    .build())

                scanner.startScan()
                    .addOnSuccessListener { barcode ->
                        // Cuando se escanea correctamente, inicia una coroutina para hacer la llamada de red.
                        lifecycleScope.launch {
                            try {
                                val rawValue = barcode.rawValue
                                // Asegúrate de que rawValue no sea null antes de hacer la llamada.
                                if (rawValue != null) {
                                    val productResponse = service.getProducto(rawValue)
                                    // Usar los datos obtenidos para actualizar la UI
                                    val nombre = productResponse.product_name
                                    val carbohydrates_100g = productResponse.nutriments.carbohydrates_100g
                                    bindingo.labelPrueba.text = "Nombre: $nombre, Carbohidratos cada 100g: $carbohydrates_100g"
                                } else {
                                    showErrorDialog("No se detectó ningún código de barras.")
                                }
                            } catch (e: Exception) {
                                // En caso de error en la llamada de red, muestra un diálogo de error.
                                showErrorDialog("Error al obtener los datos del producto." + e.message)
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception

                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Título del Popup")
                        builder.setMessage(e.message)

                        // Botón de acción positiva, por ejemplo, "Aceptar"
                        builder.setPositiveButton("Aceptar") { dialog, which ->
                            // Acción al hacer clic en "Aceptar"
                        }

                        // Botón de acción negativa, por ejemplo, "Cancelar"
                        builder.setNegativeButton("Cancelar") { dialog, which ->
                            // Acción al hacer clic en "Cancelar"
                        }

                        // Crear y mostrar el AlertDialog
                        builder.create().show()
                    }


                }

            bindingo.buttonCalcular.id -> {
                val alimento = bindingo.autoCompleteTextViewAlimento.text.toString();
                val gramos = bindingo.textInputEditTextGramos.text.toString();
                calcularRaciones(alimento, gramos);
            }

            bindingo.buttonLimpiar.id -> {
                VM.totalCarbohidratos = 0.0;
                VM.totalIndiceGlucemicoPonderado = 0.0;
                VM.totalCarbohidratosPonderados = 0.0;
                bindingo.labelCarbohidratos.text = "Carbohidratos totales: 0.0";
                bindingo.labelIndiceGlucemico.text = "Índice glucémico medio: 0.0";
                bindingo.autoCompleteTextViewAlimento.setText("");
                bindingo.textInputEditTextGramos.setText("");
            }

        }


        }
/*
    fun obtenerDatosCarbohidratos(codigoBarras: String) {
        val thread = Thread {
            try {
                val url = URL("https://world.openfoodfacts.org/api/v2/product/$codigoBarras")
                val urlConnection = url.openConnection() as HttpURLConnection
                try {
                    val inStream = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = inStream.readText()
                    // Aquí deberías parsear la respuesta JSON
                    // Por ejemplo, usando JSONObject (incorporado en Android) pero puedes usar Gson o Moshi
                    val jsonResponse = JSONObject(response)
                    val product = jsonResponse.getJSONObject("product")
                    val nombre = product.getString("product_name")
                    val carbohydrates_100g = product.getJSONObject("nutriments").getDouble("carbohydrates_100g")

                    runOnUiThread {
                        // Actualizar la UI con el nombre y los carbohidratos del producto
                        // Por ejemplo, actualizar un TextView
                        bindingo.labelPrueba.text = "Nombre: $nombre, Carbohidratos cada 100g: $carbohydrates_100g";
                    }


                    // Luego, procesa estos datos como necesites
                    // Recuerda que este código NO se ejecuta en el hilo principal, así que para actualizar la UI necesitarás usar runOnUiThread o similar

                } finally {
                    urlConnection.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Maneja el error, por ejemplo, mostrando un mensaje al usuario
            }
        }
        thread.start()
    }


 */
private fun showErrorDialog(message: String) {
    AlertDialog.Builder(this)
        .setTitle("Error")
        .setMessage(message)
        .setPositiveButton("Aceptar") { dialog, which -> dialog.dismiss() }
        .show()
}


}