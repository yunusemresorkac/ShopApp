package com.yeslabapps.eshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yeslabapps.eshop.databinding.ProductItemBinding
import com.yeslabapps.eshop.model.Product

class ProductAdapter(private val productList : ArrayList<Product>, val context: Context,val onClick:OnClick) : RecyclerView.Adapter<ProductAdapter.MyHolder>() {
    class MyHolder(val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val product = productList[position]

        holder.binding.name.text = product.title
        holder.binding.price.text = product.price.toString()+ "$"
        Picasso.get().load(product.thumbnail).into(holder.binding.image)


        holder.binding.addToCart.setOnClickListener {
            onClick.onClick(productList[position])
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


    interface OnClick{
        fun onClick(product: Product)
    }

}