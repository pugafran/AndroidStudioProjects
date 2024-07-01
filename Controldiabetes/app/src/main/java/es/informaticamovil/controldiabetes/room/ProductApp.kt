package es.informaticamovil.controldiabetes.room

import android.app.Application
import androidx.room.Room

class ProductApp : Application() {

    lateinit var room: ProductDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        room = Room.databaseBuilder(
            applicationContext,
            ProductDatabase::class.java, "product"
        ).build()
    }
}
