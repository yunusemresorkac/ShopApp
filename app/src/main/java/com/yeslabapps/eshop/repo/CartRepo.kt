package com.yeslabapps.eshop.repo

import androidx.lifecycle.LiveData
import com.yeslabapps.eshop.db.CartDao
import com.yeslabapps.eshop.model.Cart

class CartRepo(private val cartDao: CartDao) {

    val allNotes: LiveData<List<Cart>> = cartDao.getAll()



}