package es.imovil.practicapersistencia

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import es.imovil.practicapersistencia.databinding.ItemViewBinding



class ViewHolder(val itemViewBinding: ItemViewBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(book: Book) {
            with(itemViewBinding) {
                author.text = book.author
                title.text = book.title
                isbn.text = book.isbn
                editor.text = book.editorial
                price.text = book.price.toString()



            }

        }

    }


