package es.informaticamovil.controldiabetes.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {

    @Query("SELECT * FROM ProductBDD")
    suspend fun getAll(): List<ProductBDD>
    @Query("SELECT * FROM ProductBDD WHERE Id = :id")
    suspend fun getById(id: Int): ProductBDD
    @Update
    suspend fun update(product: ProductBDD)
   @Insert
    suspend fun insert(products: ProductBDD)

    @Query("SELECT * FROM ProductBDD WHERE nombre LIKE :name")
    suspend fun filterByName(name: String): List<ProductBDD>

    @Query("SELECT * FROM ProductBDD WHERE carbohidratos = :carbohydrates")
    suspend fun filterByCarbohydrates(carbohydrates: Double): List<ProductBDD>

    @Query("SELECT * FROM ProductBDD WHERE fecha LIKE :date")
    suspend fun filterByDate(date: String): List<ProductBDD>

    @Query("""
        SELECT * FROM ProductBDD 
        WHERE (:name IS NULL OR nombre LIKE :name) 
        AND (:carbohydrates IS NULL OR carbohidratos = :carbohydrates)
        AND (:date IS NULL OR fecha LIKE :date)
    """)
    suspend fun filterProducts(name: String?, carbohydrates: Double?, date: String?): List<ProductBDD>
}
