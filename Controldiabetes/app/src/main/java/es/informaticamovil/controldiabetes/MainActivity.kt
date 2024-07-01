package es.informaticamovil.controldiabetes

import Product
import android.R
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
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
import es.informaticamovil.controldiabetes.room.ProductApp
import es.informaticamovil.controldiabetes.room.ProductBDD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

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

    val app by lazy { applicationContext as ProductApp }

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


            es.informaticamovil.controldiabetes.R.id.nav_history -> {
                if (sharedPreferences.getBoolean("save_history", false)){
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                }

                else{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Tienes el historial desactivado")
                    builder.setMessage("Para poder usar el historial actívalo en el apartado de Ajustes.")
                    builder.create().show()
                }


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

        VM.totalCarbohidratos += carbohidratos
        VM.totalIndiceGlucemicoPonderado += indiceGlucemico * carbohidratos
        VM.totalCarbohidratosPonderados += carbohidratos

        val indiceGlucemicoMedio = if (VM.totalCarbohidratos == 0.0) 0.0 else VM.totalIndiceGlucemicoPonderado / VM.totalCarbohidratosPonderados

        bindingo.labelCarbohidratos.text = "Carbohidratos totales: ${"%.2f".format(VM.totalCarbohidratos)}";
        bindingo.labelIndiceGlucemico.text = "Índice glucémico medio: ${"%.2f".format(indiceGlucemicoMedio)}";

    }
    fun actualizarUI() {

        val imageView = bindingo.imageView

        // Cargar la imagen usando Glide
        Glide.with(this@MainActivity)
            .load(VM.image)
            .fitCenter() // Ajusta la imagen para que se ajuste dentro del ImageView sin recortar
            .into(imageView)

        val ecoscoreContext = when (VM.product.ecoscore_grade) {
            "a" -> "A - Muy bueno"
            "b" -> "B - Bueno"
            "c" -> "C - Regular"
            "d" -> "D - Malo"
            "e" -> "E - Muy malo"
            else -> "No clasificado"
        }

        val novaContext = when (VM.product.nova_group) {
            1 -> "1 - Alimentos no procesados o mínimamente"
            2 -> "2 - Ingredientes culinarios procesados"
            3 -> "3 - Alimentos procesados"
            4 -> "4 - Alimentos y bebidas ultraprocesados"
            else -> "No clasificado"
        }
        var analysis_tags = "";

        if(VM.product.ingredients_analysis_tags.contains("en:palm-oil-free")) {
            analysis_tags += "Sin aceite de palma, "

        }
        else if(VM.product.ingredients_analysis_tags.contains("en:palm-oil")) {
            analysis_tags += "Con aceite de palma, "
        }

        else
        {
            analysis_tags += "Sin información de si contiene aceite de palma, "
        }

        if(VM.product.ingredients_analysis_tags.contains("en:vegan")) {
            analysis_tags += "vegano, "

        }
        else if(VM.product.ingredients_analysis_tags.contains("en:non-vegan")) {
            analysis_tags += "no vegano, "
        }

        else{
            analysis_tags += "sin información de si es vegano, "
        }

        if(VM.product.ingredients_analysis_tags.contains("en:vegetarian")) {
            analysis_tags += "vegetariano."

        }
        else if(VM.product.ingredients_analysis_tags.contains("en:non-vegetarian")) {
            analysis_tags += "no vegetariano."
        }

        else{
            analysis_tags += "sin información de si es vegetariano."
        }
        if(VM.product.product_name != "")
        {
            bindingo.labelPrueba.text = "Nombre: ${VM.product.product_name} \nCarbohidratos cada 100g: ${VM.product.nutriments.carbohydrates_100g} \nMarca: ${VM.product.brands} \nClasificación NOVA: $novaContext \nÍndice glucémico: ${VM.indiceGlucemico}\nEcoscore: $ecoscoreContext \nAnálisis de ingredientes: $analysis_tags"
        }

        else
        {
            bindingo.labelPrueba.text = ""
        }
        bindingo.labelCarbohidratos.text = "Carbohidratos totales: ${"%.2f".format(VM.totalCarbohidratos)}";

        if(!((VM.totalIndiceGlucemicoPonderado / VM.totalCarbohidratosPonderados).equals(Double.NaN)))
            bindingo.labelIndiceGlucemico.text = "Índice glucémico medio: ${"%.2f".format(VM.totalIndiceGlucemicoPonderado / VM.totalCarbohidratosPonderados)}";
        else
            bindingo.labelIndiceGlucemico.text = "Índice glucémico medio: 0.0";
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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {
            bindingo.buttonBarras.id -> {
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
                                if (rawValue != null) {
                                    val productResponse = service.getResponse(rawValue).product
                                    // Usar los datos obtenidos para actualizar la UI
                                    VM.product.product_name = productResponse.product_name
                                    VM.product.nutriments.carbohydrates_100g = productResponse.nutriments.carbohydrates_100g
                                    VM.product.brands = productResponse.brands
                                    VM.product.nova_group = productResponse.nova_group
                                    VM.product.ecoscore_grade = productResponse.ecoscore_grade
                                    VM.product.ingredients_analysis_tags = productResponse.ingredients_analysis_tags

                                    val ecoscoreContext = when (VM.product.ecoscore_grade) {
                                        "a" -> "A - Muy bueno"
                                        "b" -> "B - Bueno"
                                        "c" -> "C - Regular"
                                        "d" -> "D - Malo"
                                        "e" -> "E - Muy malo"
                                        else -> "No clasificado"
                                    }

                                    Log.d("HB", "Ecoscore: ${productResponse.ecoscore_grade}")

                                    val novaContext = when (VM.product.nova_group) {
                                        1 -> "1 - Alimentos no procesados o mínimamente"
                                        2 -> "2 - Ingredientes culinarios procesados"
                                        3 -> "3 - Alimentos procesados"
                                        4 -> "4 - Alimentos y bebidas ultraprocesados"
                                        else -> "No clasificado"
                                    }
                                    var analysis_tags = "";

                                    if(VM.product.ingredients_analysis_tags.contains("en:palm-oil-free")) {
                                        analysis_tags += "Sin aceite de palma, "

                                    }
                                    else if(VM.product.ingredients_analysis_tags.contains("en:palm-oil")) {
                                        analysis_tags += "Con aceite de palma, "
                                    }

                                    else
                                    {
                                        analysis_tags += "Sin información de si contiene aceite de palma, "
                                    }

                                    if(VM.product.ingredients_analysis_tags.contains("en:vegan")) {
                                        analysis_tags += "vegano, "

                                    }
                                    else if(VM.product.ingredients_analysis_tags.contains("en:non-vegan")) {
                                        analysis_tags += "no vegano, "
                                    }

                                    else{
                                        analysis_tags += "sin información de si es vegano, "
                                    }

                                    if(VM.product.ingredients_analysis_tags.contains("en:vegetarian")) {
                                        analysis_tags += "vegetariano."

                                    }
                                    else if(VM.product.ingredients_analysis_tags.contains("en:non-vegetarian")) {
                                        analysis_tags += "no vegetariano."
                                    }

                                    else{
                                        analysis_tags += "sin información de si es vegetariano."
                                    }

                                    VM.indiceGlucemico = getChatGPTResponse("Sabiendo que este producto es ${productResponse.product_name}, y que sus carbohidratos cada 100g son ${productResponse.nutriments.carbohydrates_100g}, Estima su indice glucémico, responde solo con el número entero, no quiero texto solo una respuesta numérica.")?.toInt()
                                        ?: 0


                                    VM.totalIndiceGlucemicoPonderado += VM.indiceGlucemico * VM.product.nutriments.carbohydrates_100g

                                    VM.image = productResponse.image_url
                                    val imageView = bindingo.imageView

                                    // Cargar la imagen usando Glide
                                    Glide.with(this@MainActivity)
                                        .load(VM.image)
                                        .fitCenter() // Ajusta la imagen para que se ajuste dentro del ImageView sin recortar
                                        .into(imageView)


                                    bindingo.labelPrueba.text = "Nombre: ${VM.product.product_name} \nCarbohidratos cada 100g: ${VM.product.nutriments.carbohydrates_100g} \nMarca: ${VM.product.brands} \nClasificación NOVA: $novaContext \nÍndice glucémico: ${VM.indiceGlucemico}\nEcoscore: $ecoscoreContext \nAnálisis de ingredientes: $analysis_tags"
                                    VM.totalCarbohidratos += VM.product.nutriments.carbohydrates_100g
                                    VM.totalCarbohidratosPonderados += VM.product.nutriments.carbohydrates_100g
                                    bindingo.labelCarbohidratos.text = "Carbohidratos totales: ${"%.2f".format(VM.totalCarbohidratos)}";
                                    bindingo.labelIndiceGlucemico.text = "Índice glucémico medio: ${"%.2f".format(VM.totalIndiceGlucemicoPonderado / VM.totalCarbohidratosPonderados)}";

                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                                    val currentDateTime = LocalDateTime.now().format(formatter)



                                    if (sharedPreferences.getBoolean("save_history", false))
                                        app.room.productDao().insert(ProductBDD(0, VM.product.product_name, VM.product.nutriments.carbohydrates_100g, VM.product.brands, VM.product.nova_group, VM.product.ecoscore_grade, VM.product.ingredients_analysis_tags, currentDateTime))

                                } else {
                                    showErrorDialog("No se detectó ningún código de barras.")
                                }
                            }  catch (e: JsonSyntaxException) {
                                        // Registro detallado del error JSON
                                        showErrorDialog("Error al obtener los datos del producto." + e.message)
                                        null
                            }  catch (e: IOException) {
                                        // Manejar errores de I/O
                                        showErrorDialog("Error al obtener los datos del producto." + e.message + e.toString())
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
                        builder.setTitle("Advertencia")
                        builder.setMessage(e.message)


                        builder.setPositiveButton("Aceptar") { dialog, which ->
                            // Acción al hacer clic en "Aceptar"
                        }

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
                VM.product = Product();
                VM.image = "";
                actualizarUI();
            }

        }


        }

private fun showErrorDialog(message: String) {
    AlertDialog.Builder(this)
        .setTitle("Error")
        .setMessage(message)
        .setPositiveButton("Aceptar") { dialog, which -> dialog.dismiss() }
        .show()
}


}