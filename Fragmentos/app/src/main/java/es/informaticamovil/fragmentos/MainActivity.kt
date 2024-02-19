package es.informaticamovil.fragmentos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment;
import es.informaticamovil.fragmentos.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    val bindingo: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater);
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingo.root);
    }
}