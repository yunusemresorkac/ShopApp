package com.yeslabapps.eshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.voice.VoiceInteractionSession.ActivityId
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeslabapps.eshop.adapter.ProductAdapter
import com.yeslabapps.eshop.databinding.ActivityMainBinding
import com.yeslabapps.eshop.db.AppDatabase
import com.yeslabapps.eshop.model.Cart
import com.yeslabapps.eshop.model.Product
import com.yeslabapps.eshop.view.CartActivity
import com.yeslabapps.eshop.viewmodel.ProductViewModel
import kotlinx.coroutines.*
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class MainActivity : AppCompatActivity(),ProductAdapter.OnClick {

    private lateinit var  binding: ActivityMainBinding
    private lateinit var voiceAdapter: ProductAdapter
    private val viewModel by viewModel<ProductViewModel>()
    private lateinit var appDb : AppDatabase




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = AppDatabase.getDatabase(this)

        val layoutManager : RecyclerView.LayoutManager = GridLayoutManager(this,2)
        binding.recyclerView.layoutManager = layoutManager
        viewModel.getDataFromAPI()
        observeLiveData()



        binding.myCart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java))
        }

    }

    private fun observeLiveData() {
        viewModel.productList.observe(this, Observer {products ->

            products?.let {
                binding.recyclerView.visibility = View.VISIBLE
                voiceAdapter = ProductAdapter(ArrayList(products.data ?: arrayListOf()),this,this)
                binding.recyclerView.adapter = voiceAdapter
            }

        })

        viewModel.productError.observe(this, Observer { error->
            error?.let {
                if(it.data == true) {
                    //binding.cryptoErrorText.visibility = View.VISIBLE
                } else {
                    //binding.cryptoErrorText.visibility = View.GONE
                }
            }
        })

        viewModel.productLoading.observe(this, Observer { loading->
            loading?.let {
                if (it.data == true) {
//                    binding.progressBar.visibility = View.VISIBLE
//                    binding.recyclerView.visibility = View.GONE
                } else {
                    //binding.progressBar.visibility = View.GONE
                }
            }
        })

    }

    private fun addToCart(product: Product){
        val cart = Cart(
            product.id, product.title, product.description,product.price,product.thumbnail,1
        )
        GlobalScope.launch(Dispatchers.IO) {

            appDb.cartDao().insert(cart)
        }
        Toast.makeText(this@MainActivity,"Added to Cart",Toast.LENGTH_SHORT).show()


    }


    override fun onClick(product: Product) {
        addToCart(product)
    }


}