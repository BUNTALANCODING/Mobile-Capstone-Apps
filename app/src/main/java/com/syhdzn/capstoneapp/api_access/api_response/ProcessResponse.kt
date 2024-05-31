package com.syhdzn.capstoneapp.api_access.api_response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ApiResponse(
    val data: Data,
    val message: String,
    val status: Int
)

data class Data(
    val category: String,
    val name: String,
    val description: String,
    val image: String
)



