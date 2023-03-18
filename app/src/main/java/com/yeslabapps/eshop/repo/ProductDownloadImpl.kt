package com.yeslabapps.eshop.repo

import com.yeslabapps.eshop.model.Product
import com.yeslabapps.eshop.services.ProductApi
import com.yeslabapps.eshop.util.Resource

class ProductDownloadImpl(private val api : ProductApi) : ProductDownload {

    override suspend fun downloadProducts(): Resource<List<Product>> {
        return try {
            val response = api.getData()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error",null)
            } else {
                Resource.error("Error",null)
            }
        } catch (e: Exception) {
            Resource.error("No data!",null)
        }
    }
}