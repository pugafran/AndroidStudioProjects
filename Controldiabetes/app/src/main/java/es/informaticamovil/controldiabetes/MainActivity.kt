package es.informaticamovil.controldiabetes

import android.R
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.http.HttpException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonSyntaxException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import es.informaticamovil.controldiabetes.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {





    private val service by lazy { RetrofitClientInstance.getService() }

    private val serviceGPT by lazy { RetrofitClientInstanceGPT.getService() }

    private lateinit var drawerLayout: DrawerLayout

    val bindingo: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater);
    }

    val VM: AlimentosVM by lazy {
        ViewModelProvider(this).get(AlimentosVM::class.java);
    }

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingo.root);

        drawerLayout = bindingo.drawerLayout ?: throw IllegalStateException("DrawerLayout is null")
        bindingo.navView?.setNavigationItemSelectedListener(this)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val nombresAlimentos = VM.alimentos.map { it.nombre }
        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, nombresAlimentos)
        bindingo.autoCompleteTextViewAlimento.setAdapter(adapter);
        bindingo.autoCompleteTextViewAlimento.threshold = 1; // Empieza a mostrar sugerencias después de 1 carácter.


        bindingo.buttonBarras.setOnClickListener(this);
        bindingo.buttonCalcular.setOnClickListener(this);
        bindingo.buttonLimpiar.setOnClickListener(this);


        actualizarUI()







    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            es.informaticamovil.controldiabetes.R.id.nav_settings -> {
                // Maneja la navegación a la configuración aquí
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            es.informaticamovil.controldiabetes.R.id.nav_home -> {
                // Maneja la navegación a Home aquí
                // Por ejemplo, puedes mostrar un fragmento de inicio o simplemente un mensaje
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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
        bindingo.labelIndiceGlucemico.text = "Índice glucémico medio: ${"%.2f".format(indiceGlucemicoMedio)}";

    }


    fun actualizarUI() {

        // Asegúrate de que esta URL está correctamente definida en tu clase Product
        val imageView = bindingo.imageView

        // Cargar la imagen usando Glide
        Glide.with(this@MainActivity)
            .load(VM.image)
            .fitCenter() // Ajusta la imagen para que se ajuste dentro del ImageView sin recortar
            .into(imageView)

        val novaContext = when (VM.product.nova_group) {
            1 -> "1 - Alimentos no procesados o mínimamente"
            2 -> "2 - Ingredientes culinarios procesados"
            3 -> "3 - Alimentos procesados"
            4 -> "4 - Alimentos y bebidas ultraprocesados"
            else -> "No clasificado"
        }

        bindingo.labelPrueba.text = "Nombre: ${VM.product.product_name} \nCarbohidratos cada 100g: ${VM.product.nutriments.carbohydrates_100g} \nMarca: ${VM.product.brands} \nClasificación NOVA: $novaContext \nÍndice glucémico: ${VM.indiceGlucemico}"
        bindingo.labelCarbohidratos.text = "Carbohidratos totales: ${"%.2f".format(VM.totalCarbohidratos)}";
        bindingo.labelIndiceGlucemico.text = "Índice glucémico medio: ${"%.2f".format(VM.totalIndiceGlucemicoPonderado / VM.totalCarbohidratosPonderados)}";
    }

    private suspend fun getChatGPTResponse(message: String): String? {

        if (sharedPreferences.getBoolean("use_chatgpt", false)){

        return withContext(Dispatchers.IO) {
            try {
                val messages = listOf(
                    Message("user", message)
                )
                val request = ChatGPTRequest(
                    model = "gpt-3.5-turbo",
                    messages = messages
                )

                val response = RetrofitClientInstanceGPT.api.getChatResponse(request).execute()
                if (response.isSuccessful) {
                    val chatResponse = response.body()
                    chatResponse?.choices?.firstOrNull()?.message?.content
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        }

        return "0";
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
                                    val productResponse = service.getResponse(rawValue).product
                                    // Usar los datos obtenidos para actualizar la UI
                                    VM.product.product_name = productResponse.product_name
                                    VM.product.nutriments.carbohydrates_100g = productResponse.nutriments.carbohydrates_100g
                                    VM.product.brands = productResponse.brands
                                    VM.product.nova_group = productResponse.nova_group
                                    val novaContext = when (VM.product.nova_group) {
                                        1 -> "1 - Alimentos no procesados o mínimamente"
                                        2 -> "2 - Ingredientes culinarios procesados"
                                        3 -> "3 - Alimentos procesados"
                                        4 -> "4 - Alimentos y bebidas ultraprocesados"
                                        else -> "No clasificado"
                                    }

                                    VM.indiceGlucemico = getChatGPTResponse("Sabiendo que este producto es ${productResponse.product_name}, y que sus carbohidratos cada 100g son ${productResponse.nutriments.carbohydrates_100g}, Estima su indice glucémico, responde solo con el número entero, no quiero texto solo una respuesta numérica.")?.toInt()
                                        ?: 0


                                    VM.totalIndiceGlucemicoPonderado += VM.indiceGlucemico * VM.product.nutriments.carbohydrates_100g

                                    VM.image = productResponse.image_url // Asegúrate de que esta URL está correctamente definida en tu clase Product
                                    val imageView = bindingo.imageView

                                    // Cargar la imagen usando Glide
                                    Glide.with(this@MainActivity)
                                        .load(VM.image)
                                        .fitCenter() // Ajusta la imagen para que se ajuste dentro del ImageView sin recortar
                                        .into(imageView)


                                    bindingo.labelPrueba.text = "Nombre: ${VM.product.product_name} \nCarbohidratos cada 100g: ${VM.product.nutriments.carbohydrates_100g} \nMarca: ${VM.product.brands} \nClasificación NOVA: $novaContext \nÍndice glucémico: ${VM.indiceGlucemico}"
                                    VM.totalCarbohidratos += VM.product.nutriments.carbohydrates_100g
                                    VM.totalCarbohidratosPonderados += VM.product.nutriments.carbohydrates_100g
                                    bindingo.labelCarbohidratos.text = "Carbohidratos totales: ${"%.2f".format(VM.totalCarbohidratos)}";
                                    bindingo.labelIndiceGlucemico.text = "Índice glucémico medio: ${"%.2f".format(VM.totalIndiceGlucemicoPonderado / VM.totalCarbohidratosPonderados)}";
                                } else {
                                    showErrorDialog("No se detectó ningún código de barras.")
                                }
                            }     catch (e: JsonSyntaxException) {
                        // Registro detallado del error JSON
                                showErrorDialog("Error al obtener los matos del producto." + e.message)
                                Log.d("HB", e.stackTraceToString())

                                null
                    }  catch (e: IOException) {
                        // Manejar errores de I/O
                                showErrorDialog("Error al obtener los watos del producto." + e.message + e.toString())

                                null
                    }
                            catch (e: Exception) {
                                // En caso de error en la llamada de red, muestra un diálogo de error.
                                showErrorDialog("Error al obtener los datos del producto." + e.message + e.toString())
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