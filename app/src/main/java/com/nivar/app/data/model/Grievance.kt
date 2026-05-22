package com.nivar.app.data.model

data class Grievance(
    val id: Int? = null,
    val title: String,
    val description: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val isAnonymous: Boolean = false,
    val status: String = "OPEN",
    val upvotes: Int = 0,
    val created_at: String? = null
)
