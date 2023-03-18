package com.yeslabapps.eshop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.yeslabapps.eshop.db.CartDatabase
import com.yeslabapps.eshop.model.Cart
import com.yeslabapps.eshop.repo.CartRepo

class CartViewModel (application: Application) : AndroidViewModel(application) {

    // on below line we are creating a variable
    // for our all notes list and repository
    val allNotes : LiveData<List<Cart>>
    val repository : CartRepo

    // on below line we are initializing
    // our dao, repository and all notes
    init {
        val dao = CartDatabase.getDatabase(application).cartDao()
        repository = CartRepo(dao)
        allNotes = repository.allNotes
    }


}