package es.informaticamovil.controldiabetes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import es.informaticamovil.controldiabetes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val bindingo: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater);
    }

    val VM: ContadorVM by lazy {
        ViewModelProvider(this).get(ContadorVM::class.java);
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_EAN_13)
            .enableAutoZoom()
            .build();

        val scanner = GmsBarcodeScanning.getClient(this, options);




    }

    override fun onClick(v: View?) {
        when(v?.id) {
        }
        }
}