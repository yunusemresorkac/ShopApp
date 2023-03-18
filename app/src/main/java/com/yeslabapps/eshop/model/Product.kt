package com.yeslabapps.eshop.model

data class Product( val id : Int,
                    val title : String,
                    val description : String,
                    val price : Double,
                    val discountPercentage : Double,
                    val rating : Double,
                    val brand : String,
                    val category :  String,
                    val stock : Int,
                    val thumbnail : String)
