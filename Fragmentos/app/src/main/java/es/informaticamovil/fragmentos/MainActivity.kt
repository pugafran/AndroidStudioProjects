package es.informaticamovil.fragmentos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment;
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import es.informaticamovil.fragmentos.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity(), View.OnClickListener {

    var isChanged: Boolean = false;

    val bindingo: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater);
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingo.root);

        bindingo.buttonsito.setOnClickListener(this);

       supportFragmentManager.setFragmentResultListener("requestKey2", this) { _, bundle ->
            bindingo.titulo.text = bundle.getString("bundleKey2")
        }



    }

    override fun onClick(v: View?) {
        when(v?.id) {
            bindingo.buttonsito.id -> {




                supportFragmentManager.commit {

                    isChanged = if (isChanged) {
                        replace(bindingo.fragmentContainerView.id, FirstFragment())
                        replace(bindingo.fragmentContainerView2.id, SecondFragment())
                        false
                    } else {
                        replace(bindingo.fragmentContainerView.id, SecondFragment())
                        replace(bindingo.fragmentContainerView2.id, FirstFragment())
                        true
                    }
                }
            }
            }

            }

    }
