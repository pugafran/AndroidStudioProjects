package es.imovil.practicapersistencia

import androidx.recyclerview.widget.DiffUtil

data class Book(var title:String,
                var author:String,
                var isbn:String? = null,
                var editorial:String? = null,
                var price:Float) {

    object DIFF_CALLBACK: DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.isbn == newItem.isbn
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

    }
}
