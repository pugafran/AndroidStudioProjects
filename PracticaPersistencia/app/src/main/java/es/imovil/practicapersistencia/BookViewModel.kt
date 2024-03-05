package es.imovil.practicapersistencia

import android.app.Application
import android.provider.Telephony.Mms.Part.FILENAME
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class BookViewModel(private val application: Application) : AndroidViewModel(application) {
    var bookList = mutableListOf<Book>()

    fun getListSize(): Int {
        return bookList.size
    }

    fun getBook(position: Int): Book {
        return bookList[position]
    }

    fun addBook(book: Book) {
        if(!bookList.contains(book)){
            bookList.add(book)
        }

    }

    fun setArchivename(archivename: String) {
        val sp = PreferenceManager.getDefaultSharedPreferences(application)
        val editor = sp.edit()
        editor.putString("archivename", archivename)
        editor.apply()
    }

    fun restoreBookList() {
        val file = File(application.filesDir, FILENAME)
        var lista:List<Book> = emptyList()

        if (file.exists()) {
            val contenido = file.readText()
            val listType = object : TypeToken<List<Book>>() {}.type
            lista = Gson().fromJson(contenido, listType)
            bookList.addAll(lista)
        }
    }

    fun saveBookList() {
        val file = File(application.filesDir, FILENAME)
        val contenido = Gson().toJson(bookList)
        file.writeText(contenido)
    }





}