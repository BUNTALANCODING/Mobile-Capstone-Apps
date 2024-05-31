package com.syhdzn.capstoneapp.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syhdzn.capstoneapp.api_access.ApiClient
import com.syhdzn.capstoneapp.api_access.api_response.Datahistori
import com.syhdzn.capstoneapp.api_access.api_response.HistoriResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HistoryViewModel : ViewModel() {
    private val _historiList = MutableLiveData<List<Datahistori>>()
    val historiList: LiveData<List<Datahistori>> get() = _historiList

    fun fetchHistory() {
        viewModelScope.launch {
            try {
                val response = ApiClient.getApiService().getHistory()
                handleShopResponse(response)
            } catch (e: HttpException) {

            } catch (e: Throwable) {

            }
        }
    }

    private fun handleShopResponse(response: retrofit2.Response<HistoriResponse>) {
        if (response.isSuccessful) {
            val historiList = response.body()?.data ?: emptyList()
            _historiList.value = historiList
        } else {

        }
    }
}
