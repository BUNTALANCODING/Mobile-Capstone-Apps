package com.syhdzn.capstoneapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syhdzn.capstoneapp.api_access.ApiClient
import com.syhdzn.capstoneapp.api_access.ApiService
import com.syhdzn.capstoneapp.api_access.api_response.FoodsItem
import com.syhdzn.capstoneapp.api_access.api_response.FoodsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val apiService: ApiService = ApiClient.getApiService()

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int>
        get() = _currentPage

    private val _foodList = MutableLiveData<List<FoodsItem>>()
    val foodList: LiveData<List<FoodsItem>>
        get() = _foodList

    fun setCurrentPage(position: Int) {
        _currentPage.value = position
    }
    init {
        fetchFoods()
    }

    fun searchFoodsByName(name: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getFoods()
                if (response.isSuccessful) {
                    val foodsResponse = response.body()
                    if (foodsResponse != null) {
                        val filteredList = foodsResponse.data.filter { it.name.contains(name, ignoreCase = true) }
                        _foodList.value = filteredList
                    } else {
                        _foodList.value = emptyList()
                    }
                } else {
                    _foodList.value = emptyList()
                }
            } catch (e: Exception) {
                _foodList.value = emptyList()
            }
        }
    }

    fun fetchFoods() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.getApiService().getFoods()
                withContext(Dispatchers.Main) {
                    handleApiResponse(response)
                }
            } catch (e: Exception) {

            }
        }
    }


    private fun handleApiResponse(response: Response<FoodsResponse>) {
        if (response.isSuccessful) {
            val foodsResponse = response.body()
            if (foodsResponse != null && foodsResponse.data.isNotEmpty()) {
                _foodList.value = foodsResponse.data
            } else {

            }
        } else {

        }
    }


}
