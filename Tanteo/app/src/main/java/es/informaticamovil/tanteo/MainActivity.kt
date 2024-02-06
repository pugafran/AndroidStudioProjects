package es.informaticamovil.tanteo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import es.informaticamovil.tanteo.databinding.ActivityMain2Binding
import es.informaticamovil.tanteo.databinding.ActivityMainBinding

const val VISITANTE = "Visitante";
const val LOCAL = "Local";

lateinit var BINDING : MainActivity2;

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onSaveInstanceState(outState: Bundle) {



        outState.putInt("DeV1",VM.puntajeVisitante.de1);
        outState.putInt("DeV2",VM.puntajeVisitante.de2);
        outState.putInt("DeV3",VM.puntajeVisitante.de3);

        outState.putInt("DeL1",VM.puntajeLocal.de1);
        outState.putInt("DeL2",VM.puntajeLocal.de2);
        outState.putInt("DeL3",VM.puntajeLocal.de3);



        super.onSaveInstanceState(outState);
    }


    private fun actualizarPuntaje() {
        bindingo.labelLocalValue.text = VM.puntajeLocal.total.toString()
        bindingo.labelVisitanteValue.text = VM.puntajeVisitante.total.toString()
        Log.d("Tanteo", "Puntaje Local: $VM.puntajeLocal, Puntaje Visitante: $VM.puntajeVisitante")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            bindingo.botonsito.id -> {
                val intent = Intent(this,MainActivity2::class.java ).apply {
                    putExtra(LOCAL, VM.puntajeLocal);
                    putExtra(VISITANTE, VM.puntajeVisitante);

                }
                startActivity(intent);
            }
            bindingo.botonsito1.id -> {
                VM.puntajeLocal.de1 += 1;
                actualizarPuntaje()
            }
            bindingo.botonsito2.id -> {
                VM.puntajeLocal.de2 += 1;
                actualizarPuntaje()
            }
            bindingo.botonsito3.id -> {
                VM.puntajeLocal.de3 += 1;
                actualizarPuntaje()
            }
            bindingo.botonsito1V.id -> {
                VM.puntajeVisitante.de1 += 1;
                actualizarPuntaje()
            }
            bindingo.botonsito2V.id -> {
                VM.puntajeVisitante.de2 += 1
                actualizarPuntaje()
            }
            bindingo.botonsito3V.id -> {
                VM.puntajeVisitante.de3 += 1
                actualizarPuntaje()
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        VM.puntajeVisitante.de1 = savedInstanceState.getInt("DeV1",0);
        VM.puntajeVisitante.de2 = savedInstanceState.getInt("DeV2",0);
        VM.puntajeVisitante.de3 = savedInstanceState.getInt("DeV3",0);

        VM.puntajeLocal.de1 = savedInstanceState.getInt("DeL1",0);
        VM.puntajeLocal.de2 = savedInstanceState.getInt("DeL2",0);
        VM.puntajeLocal.de3 = savedInstanceState.getInt("DeL3",0);

        actualizarPuntaje();


        super.onRestoreInstanceState(savedInstanceState)
    }






    /* Vinculado a MainActivity, poniendo en el el gradle:
        viewBinding{
        enable = true;
    }
     */
    val bindingo: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater);
    }

    val VM: ContadorVM by lazy {
        ViewModelProvider(this).get(ContadorVM::class.java);
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Le estoy diciendo que la pantalla la saque de la imagen de esta clase
        setContentView(bindingo.root);

        //BINDING = MainActivity2.inflate(layoutInflater);




        bindingo.botonsito.setOnClickListener(this);
        bindingo.botonsito1.setOnClickListener(this);
        bindingo.botonsito2.setOnClickListener(this);
        bindingo.botonsito3.setOnClickListener(this);
        bindingo.botonsito1V.setOnClickListener(this);
        bindingo.botonsito2V.setOnClickListener(this);
        bindingo.botonsito3V.setOnClickListener(this);

/*

        bindingo.botonsito.setOnClickListener{
            val intent = Intent(this,MainActivity2::class.java ).apply {

                putExtra(LOCAL, VM.puntajeLocal);
                putExtra(VISITANTE, VM.puntajeVisitante);
                startActivity(intent);

            }

        }

        bindingo.botonsito1.setOnClickListener {
            VM.puntajeLocal.de1 += 1;
            actualizarPuntaje()
        }

        bindingo.botonsito2.setOnClickListener {
            VM.puntajeLocal.de2 += 1;
            actualizarPuntaje()
        }

        bindingo.botonsito3.setOnClickListener {
            VM.puntajeLocal.de3 += 1;
            actualizarPuntaje()
        }

        bindingo.botonsito1V.setOnClickListener {
            VM.puntajeVisitante.de1 += 1;
            actualizarPuntaje()
        }

        bindingo.botonsito2V.setOnClickListener {
            VM.puntajeVisitante.de2 += 1
            actualizarPuntaje()
        }

        bindingo.botonsito3V.setOnClickListener {
            VM.puntajeVisitante.de3 += 1
            actualizarPuntaje()
        }
    }

*/


}}