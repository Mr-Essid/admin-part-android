package com.example.pointage_application.models

data class UserRequest(
    val email: String,
    val gender: String,
    val isadmin: Boolean? = null,
    val mobile: String,
    val name: String,
    val password: String? = null,
    val uid: String
)