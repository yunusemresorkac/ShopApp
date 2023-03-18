package com.yeslabapps.eshop.adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yeslabapps.eshop.databinding.CartItemBinding
import com.yeslabapps.eshop.databinding.ProductItemBinding
import com.yeslabapps.eshop.model.Cart

class CartAdapter(private val cartList : ArrayList<Cart>, val context: Context, val onClick:OnClick) : RecyclerView.Adapter<CartAdapter.MyHolder>() {

    class MyHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val cart = cartList[position]

        holder.binding.name.text = cart.title
        Picasso.get().load(cart.image).into(holder.binding.image)
        holder.binding.price.text = cart.price.toString()
        holder.binding.quantity.text = cart.quantity.toString()

        if (cart.quantity ==1){
            holder.binding.totalPrice.visibility= View.GONE
        }else{
            holder.binding.totalPrice.visibility= View.VISIBLE
            holder.binding.totalPrice.text = "Total Price: ${(cart.price!!.toInt() * cart.quantity!!).toString()}"
        }

        holder.binding.increase.setOnClickListener {
            onClick.increase(cartList[position])
        }
        holder.binding.decrease.setOnClickListener {
            onClick.decrease(cartList[position])
        }
    }
    fun updateList(newList: List<Cart>) {
        // on below line we are clearing
        // our notes array list
        cartList.clear()
        // on below line we are adding a
        // new list to our all notes list.
        cartList.addAll(newList)
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


    interface OnClick{
        fun increase(cart: Cart)

        fun decrease(cart: Cart)

    }

}