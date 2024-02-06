package es.informaticamovil.primera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log

private const val TAG = "Actividad 2";

class ActivityMain2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "Estoy en onCreate");
        setContentView(R.layout.activity_main2)



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