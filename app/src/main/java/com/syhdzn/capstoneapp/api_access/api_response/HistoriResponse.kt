package com.syhdzn.capstoneapp.api_access.api_response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class HistoriResponse(
    val `data`: List<Datahistori>,
    val message: String,
    val status: Int
)
@Parcelize
data class Datahistori(
    val category: String,
    val created_at: String,
    val description: String,
    val id: String,
    val image: String,
    val name: String
): Parcelable