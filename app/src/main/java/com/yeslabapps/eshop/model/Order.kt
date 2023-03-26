package com.yeslabapps.eshop.model

data class Order(val cartList: List<Cart>,val firstName: String,val lastName:String,val phone :String ,val address:String,val totalPrice : Double)
