package es.informaticamovil.anr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import es.informaticamovil.anr.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity(), View.OnClickListener {

    val bindingo: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater);
    }



    private var contador = 0;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(bindingo.root);
        bindingo.button.setOnClickListener(this);
        bindingo.button2.setOnClickListener(this);

    }



override fun onClick(v: View?) {
    val handler = Handler(Looper.getMainLooper());
        when(v?.id){

            bindingo.button.id -> contador++;
            bindingo.button2.id -> //Hilo().start(); //Thread.sleep(1000);
            {
                val mihilo = Thread {
                    Thread.sleep(3000)
                    handler.post {
                        bindingo.textView.text = "Cambiada desde el hilo";
                    }
                }.start();

            }

        }

    }
}