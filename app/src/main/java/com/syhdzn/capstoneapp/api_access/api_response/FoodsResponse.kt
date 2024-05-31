package com.syhdzn.capstoneapp.api_access.api_response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodsItem(
    val id: Int,
    val name: String,
    val description: String,
    val image: String
): Parcelable

data class FoodsResponse(
    val data: List<FoodsItem>,
    val message: String,
    val status: Int
)
