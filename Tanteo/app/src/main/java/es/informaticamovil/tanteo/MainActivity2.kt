package es.informaticamovil.tanteo

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import es.informaticamovil.tanteo.databinding.ActivityMain2Binding
import es.informaticamovil.tanteo.databinding.ActivityMainBinding


class MainActivity2 : AppCompatActivity() {

    val bindingo: ActivityMain2Binding by lazy{
        ActivityMain2Binding.inflate(layoutInflater);
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(bindingo.root);

        VM.puntajeLocal = intent.getParcelableExtra(LOCAL, TeamPoints::class.java) ?: TeamPoints(2,2,2);
        Log.d("Tanteo", "Puntaje Local: ${VM.puntajeLocal.total}");
        VM.puntajeVisitante = intent.getParcelableExtra(VISITANTE, TeamPoints::class.java) ?: TeamPoints(3,3,3);
        bindingo.labelLocalValue2.text = VM.puntajeLocal.total.toString();
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main2);





    }



    val VM: ContadorVM by lazy {
        ViewModelProvider(this).get(ContadorVM::class.java);
    }

    // Obtén el intent que ha iniciado esta actividad


}

