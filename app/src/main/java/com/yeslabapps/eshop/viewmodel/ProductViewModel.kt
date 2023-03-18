package com.yeslabapps.eshop.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeslabapps.eshop.model.Product
import com.yeslabapps.eshop.repo.ProductDownload
import com.yeslabapps.eshop.util.Resource
import kotlinx.coroutines.*

class ProductViewModel(
    private val cryptoDownloadRepository : ProductDownload
) : ViewModel() {

    val productList = MutableLiveData<Resource<List<Product>>>()
    val productError = MutableLiveData<Resource<Boolean>>()
    val productLoading = MutableLiveData<Resource<Boolean>>()
    private var job : Job? = null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}")
        productError.value = Resource.error(throwable.localizedMessage ?: "error!",data = true)
    }




    fun getDataFromAPI() {
        productLoading.value = Resource.loading(true)

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val resource = cryptoDownloadRepository.downloadProducts()
            withContext(Dispatchers.Main) {
                resource.data?.let {
                    productLoading.value = Resource.loading(false)
                    productError.value = Resource.error("",data = false)
                    productList.value = resource
                }
            }
        }


    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
