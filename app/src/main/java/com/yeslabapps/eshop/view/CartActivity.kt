package com.yeslabapps.eshop.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yeslabapps.eshop.adapter.CartAdapter
import com.yeslabapps.eshop.databinding.ActivityCartBinding
import com.yeslabapps.eshop.db.AppDatabase
import com.yeslabapps.eshop.model.Cart
import com.yeslabapps.eshop.util.Resource
import com.yeslabapps.eshop.viewmodel.CartViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartActivity : AppCompatActivity(), CartAdapter.OnClick{

    private lateinit var binding: ActivityCartBinding
    private lateinit var appDb : AppDatabase
    private lateinit var cartList : ArrayList<Cart>
    private lateinit var cartAdapter : CartAdapter
    private lateinit var viewModal: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = AppDatabase.getDatabase(this)




        binding.recyclerView.layoutManager = GridLayoutManager(this,2)

        viewModal = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[CartViewModel::class.java]
        getCart()




        binding.deleteCart.setOnClickListener {
            deleteCart()
        }


    }

    private fun getCart(){
        viewModal.allNotes.observe(this) { list ->
            list?.let {
                cartAdapter =
                    CartAdapter(list as ArrayList<Cart>, this@CartActivity, this@CartActivity)
                binding.recyclerView.adapter = cartAdapter

                if (list.isEmpty()){
                    binding.deleteCart.visibility = View.GONE
                }
            }
        }
    }


    private fun deleteCart(){
        GlobalScope.launch(Dispatchers.IO) {

            appDb.cartDao().deleteAll()
        }
    }

    override fun increase(cart: Cart) {
        GlobalScope.launch(Dispatchers.Default) {

            cart.quantity = cart.quantity?.plus(1)
            appDb.cartDao().update(cart)
        }

    }

    override fun decrease(cart: Cart) {
        GlobalScope.launch(Dispatchers.Default) {
            if (cart.quantity!! > 1){
                cart.quantity = cart.quantity?.minus(1)
                appDb.cartDao().update(cart)
            }
            if (cart.quantity == 1){
                appDb.cartDao().delete(cart)
            }

        }
    }


}