package com.syhdzn.capstoneapp.api_access

import com.syhdzn.capstoneapp.api_access.api_response.ApiResponse
import com.syhdzn.capstoneapp.api_access.api_response.DetailShopResponse
import com.syhdzn.capstoneapp.api_access.api_response.FoodsResponse
import com.syhdzn.capstoneapp.api_access.api_response.HistoriResponse

import com.syhdzn.capstoneapp.api_access.api_response.ShopResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("foods")
    suspend fun getFoods(): Response<FoodsResponse>

    @GET("shops")
    suspend fun getShops(): Response<ShopResponse>

    @GET("shops/{shopId}")
    suspend fun getShopDetails(): Response<DetailShopResponse>

    @GET("shops/{shopId}")
    suspend fun getShopDetailsById(@Path("shopId") shopId: Int): Response<DetailShopResponse>

    @Multipart
    @POST("predict")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("type") type: RequestBody
    ): Call<ApiResponse>

    @GET("users/1/history")
    suspend fun getHistory(): Response<HistoriResponse>
}

