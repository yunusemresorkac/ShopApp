package com.yeslabapps.eshop

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeslabapps.eshop.adapter.ProductAdapter
import com.yeslabapps.eshop.databinding.ActivityMainBinding
import com.yeslabapps.eshop.db.CartDatabase
import com.yeslabapps.eshop.model.Cart
import com.yeslabapps.eshop.model.Product
import com.yeslabapps.eshop.view.CartActivity
import com.yeslabapps.eshop.viewmodel.ProductViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(),ProductAdapter.OnClick {

    private lateinit var  binding: ActivityMainBinding
    private lateinit var voiceAdapter: ProductAdapter
    private val viewModel by viewModel<ProductViewModel>()
    private lateinit var appDb : CartDatabase




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = CartDatabase.getDatabase(this)

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

    private fun myAddToCart(product: Product){
        val cart = Cart(
            product.id, product.title, product.description,product.price,product.thumbnail,1
        )
        GlobalScope.launch(Dispatchers.IO) {

            appDb.cartDao().insert(cart)
        }


    }

    override fun seeDetails(product: Product) {
        showDetailsDialog( product)
    }


    override fun addToCart(product: Product) {
        myAddToCart(product)
    }

    private fun showDetailsDialog(product: Product){
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_details)

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.BOTTOM)

        val description: TextView = dialog.findViewById(R.id.description)
        description.text = product.description
    }

}