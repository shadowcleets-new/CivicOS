package com.nivar.app.data.model

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val state: String = "",
    val city: String = "",
    val isGuest: Boolean = false
)
