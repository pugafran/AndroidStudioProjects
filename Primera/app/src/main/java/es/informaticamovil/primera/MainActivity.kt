package es.informaticamovil.primera

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button

private const val TAG = "Actividad 1";


class MainActivity : AppCompatActivity() {

    //Lo asigna ya,esto se ejecuta con el onclick (?)
    val button: Button by lazy{
        findViewById(R.id.button);

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "Estoy en onCreate");
        setContentView(R.layout.activity_main)


        button.setOnClickListener(){
            val intent = Intent(this, ActivityMain2::class.java);
            startActivity(intent);
        }


    }

    override fun onStart() {
        Log.d(TAG, "Estoy en onStart");
        super.onStart()
    }

    override fun onRestart() {
        Log.d(TAG, "Estoy en onRestart");
        super.onRestart()
    }

    override fun onStop() {
        Log.d(TAG, "Estoy en onStop");
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "Estoy en onDestroy");
        super.onDestroy()
    }

    override fun onResume() {
        Log.d(TAG, "Estoy en onResume");
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "Estoy en onPause");
        super.onPause()
        Thread.sleep(3000);
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        if(savedInstanceState == null){
            Log.d(TAG, "Bundle es nulo");
        }
        else{
            Log.d(TAG, "Bundle no es nulo");

        }

        super.onRestoreInstanceState(savedInstanceState)
    }







}

