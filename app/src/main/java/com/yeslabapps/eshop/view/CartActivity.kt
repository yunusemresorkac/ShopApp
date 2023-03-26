package com.yeslabapps.eshop.view

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.yeslabapps.eshop.R
import com.yeslabapps.eshop.adapter.CartAdapter
import com.yeslabapps.eshop.databinding.ActivityCartBinding
import com.yeslabapps.eshop.db.CartDatabase
import com.yeslabapps.eshop.model.Cart
import com.yeslabapps.eshop.model.Order
import com.yeslabapps.eshop.viewmodel.CartViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class CartActivity : AppCompatActivity(), CartAdapter.OnClick{

    private lateinit var binding: ActivityCartBinding
    private lateinit var appDb : CartDatabase
    private lateinit var cartAdapter : CartAdapter
    private val viewModel by viewModel<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = CartDatabase.getDatabase(this)




        binding.recyclerView.layoutManager = GridLayoutManager(this,2)

        getCart()


        getCardSummary()


        binding.deleteCart.setOnClickListener {
            deleteCart()
        }

        binding.goPayout.setOnClickListener{
            goPayout()
        }

    }

    private fun getCart(){
        viewModel.allNotes.observe(this) { list ->
            list?.let {
                cartAdapter =
                    CartAdapter(list as ArrayList<Cart>, this@CartActivity, this@CartActivity)
                binding.recyclerView.adapter = cartAdapter

                if (list.isEmpty()){
                    binding.emptyCart.visibility = View.VISIBLE
                    binding.deleteCart.visibility = View.GONE
                    binding.cartSummaryText.visibility = View.GONE
                }else{
                    binding.emptyCart.visibility = View.GONE
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
        GlobalScope.launch(Dispatchers.IO) {
            cart.quantity = cart.quantity?.plus(1)
            appDb.cartDao().update(cart)
        }



    }

    override fun decrease(cart: Cart) {
        GlobalScope.launch(Dispatchers.IO) {
            if (cart.quantity!! >= 1){
                cart.quantity = cart.quantity?.minus(1)
            }
            if (cart.quantity!! < 1){
                appDb.cartDao().delete(cart)
            }
            appDb.cartDao().update(cart)

        }
    }


    private fun getCardSummary(){
        viewModel.allNotes.observe(this) { list ->
            list?.let {
                for (cart in list){
                    val prices = listOf(cart.price?.times(cart.quantity!!))
                    println("fiyatlar ve adetler $prices ")
                    prices.sumOf { cart.quantity!! }
                    println("toplam  ${list.sumOf { it.price?.times(it.quantity!!)!! }} ")
                    binding.cartSummaryText.text = "Total: ${list.sumOf { it.price?.times(it.quantity!!)!! }} "
                }
            }
        }
    }

    private fun goPayout(){
        val dialog = Dialog(this@CartActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_payout)

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.setBackgroundDrawableResource(R.color.white)

        val firstName: EditText = dialog.findViewById(R.id.firstName)
        val lastName: EditText = dialog.findViewById(R.id.lastName)
        val phone: EditText = dialog.findViewById(R.id.phone)
        val address: EditText = dialog.findViewById(R.id.address)
        val confirm: MaterialButton = dialog.findViewById(R.id.confirmBtn)

        viewModel.allNotes.observe(this) { list ->
            list?.let {
                for (cart in list){
                    confirm.setOnClickListener{
                        createOrder(list,firstName.text.toString(),lastName.text.toString(),phone.text.toString(),address.text.toString()
                        ,list.sumOf { it.price?.times(it.quantity!!)!!})
                    }
                }
            }
        }


    }

    private fun createOrder(cart: List<Cart>,firstName: String,lastName:String, phone :String ,address:String,totalPrice : Double){

        val reference : CollectionReference = FirebaseFirestore.getInstance().collection("Orders")
        reference.add(Order(cart,firstName,lastName,phone,address,totalPrice))
    }





}