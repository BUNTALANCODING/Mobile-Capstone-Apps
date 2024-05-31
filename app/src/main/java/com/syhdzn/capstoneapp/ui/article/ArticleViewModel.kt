package com.syhdzn.capstoneapp.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.syhdzn.capstoneapp.api_access.api_response.FoodsItem

class ArticleViewModel : ViewModel() {
    private val _foodItem = MutableLiveData<FoodsItem>()
    val foodItem: LiveData<FoodsItem> get() = _foodItem

    fun setFoodItem(item: FoodsItem) {
        _foodItem.value = item
    }
}
