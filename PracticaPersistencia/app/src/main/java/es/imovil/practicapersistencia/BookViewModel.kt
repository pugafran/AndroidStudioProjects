package es.imovil.practicapersistencia

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class BookViewModel(private val application: Application) : AndroidViewModel(application) {
    var bookList = mutableListOf<Book>()

    fun getListSize(): Int {
        return bookList.size
    }

    fun getBook(position: Int): Book {
        return bookList[position]
    }

    fun addBook(book: Book) {
        bookList.add(book)
    }




}