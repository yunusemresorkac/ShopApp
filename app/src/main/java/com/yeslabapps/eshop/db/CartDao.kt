package com.yeslabapps.eshop.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yeslabapps.eshop.model.Cart

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_table ORDER BY title DESC")
    fun getAll(): LiveData<List<Cart>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cart: Cart)

    @Delete
    suspend fun delete(cart: Cart)

    @Query("DELETE FROM cart_table")
    fun deleteAll()


    @Update
    fun update(cart: Cart)
}