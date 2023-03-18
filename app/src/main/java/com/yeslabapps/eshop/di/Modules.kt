package com.yeslabapps.eshop.di

import com.yeslabapps.eshop.repo.ProductDownload
import com.yeslabapps.eshop.repo.ProductDownloadImpl
import com.yeslabapps.eshop.services.ProductApi
import com.yeslabapps.eshop.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        val baseUrl = "https://api.npoint.io/"
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create())
            .build().create(ProductApi::class.java)
    }
    single<ProductDownload> {
        ProductDownloadImpl(get())

    }


    viewModel{
        ProductViewModel(get())
    }


}


