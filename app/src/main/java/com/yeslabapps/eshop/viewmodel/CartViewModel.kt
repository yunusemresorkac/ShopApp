package com.yeslabapps.eshop.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeslabapps.eshop.db.AppDatabase
import com.yeslabapps.eshop.db.CartDao
import com.yeslabapps.eshop.model.Cart
import com.yeslabapps.eshop.repo.CartRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel (application: Application) : AndroidViewModel(application) {

    // on below line we are creating a variable
    // for our all notes list and repository
    val allNotes : LiveData<List<Cart>>
    val repository : CartRepo

    // on below line we are initializing
    // our dao, repository and all notes
    init {
        val dao = AppDatabase.getDatabase(application).cartDao()
        repository = CartRepo(dao)
        allNotes = repository.allNotes
    }


}