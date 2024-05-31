package com.syhdzn.capstoneapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.syhdzn.capstoneapp.api_access.api_response.Datahistori

class DetailViewModel : ViewModel() {
    private val _historiItem = MutableLiveData<Datahistori>()
    val historiItem: LiveData<Datahistori> get() = _historiItem

    fun setHistoriItem(item: Datahistori) {
        _historiItem.value = item
    }
}
