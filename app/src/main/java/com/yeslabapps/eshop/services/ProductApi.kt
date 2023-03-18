package com.yeslabapps.eshop.services

import com.yeslabapps.eshop.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductApi {

    @GET("7e27a6ec39384ddad544/")
    suspend fun getData(): Response<List<Product>>

}
