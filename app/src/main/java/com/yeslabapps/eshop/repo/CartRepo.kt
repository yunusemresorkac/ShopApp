package com.yeslabapps.eshop.repo

import androidx.lifecycle.LiveData
import com.yeslabapps.eshop.db.CartDao
import com.yeslabapps.eshop.model.Cart

class CartRepo(private val cartDao: CartDao) {

    // on below line we are creating a variable for our list
    // and we are getting all the notes from our DAO class.
    val allNotes: LiveData<List<Cart>> = cartDao.getAll()

    // on below line we are creating an insert method
    // for adding the note to our database.
    suspend fun insert(cart: Cart) {
        cartDao.insert(cart)
    }

    // on below line we are creating a delete method
    // for deleting our note from database.
    suspend fun delete(cart: Cart){
        cartDao.delete(cart)
    }


}