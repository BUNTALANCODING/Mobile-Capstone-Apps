package com.syhdzn.capstoneapp.api_access.api_response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopItem(
    val id: Int,
    val image: String,
    val location: String,
    val name: String
): Parcelable
data class ShopResponse(
    val data: List<ShopItem>,
    val message: String,
    val status: Int
)


