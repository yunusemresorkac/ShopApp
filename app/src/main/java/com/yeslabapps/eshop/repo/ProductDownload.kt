package com.yeslabapps.eshop.repo

import com.yeslabapps.eshop.model.Product
import com.yeslabapps.eshop.util.Resource

interface ProductDownload {
    suspend fun downloadProducts() : Resource<List<Product>>
}