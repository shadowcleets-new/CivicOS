package com.nivar.app.data.model

data class Scheme(
    val id: Int,
    val title: String,
    val description: String,
    val ministry: String,
    val url: String?,
    val tags: List<String> = emptyList()
)
