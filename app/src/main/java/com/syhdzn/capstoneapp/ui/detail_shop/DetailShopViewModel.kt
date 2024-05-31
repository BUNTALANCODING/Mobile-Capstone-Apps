package com.syhdzn.capstoneapp.ui.detail_shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.syhdzn.capstoneapp.api_access.api_response.ShopDetail

class DetailShopViewModel : ViewModel() {
    private val _shopDetail = MutableLiveData<ShopDetail>()
    val shopDetail: LiveData<ShopDetail> get() = _shopDetail

    fun setShopDetail(detail: ShopDetail) {
        _shopDetail.value = detail
    }
}
