package com.syhdzn.capstoneapp.api_access.api_response

data class DetailShopResponse(
    val data: ShopDetail,
    val message: String,
    val status: Int
)

data class ShopDetail(
    val id: Int,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val location: String,
    val name: String,
    val foods: List<FoodDetail>
)
data class FoodDetail(
    val id: Int,
    val image: String,
    val name: String,
    val price: Double
)
