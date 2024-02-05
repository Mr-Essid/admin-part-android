package com.example.pointage_application.models

data class UserResponse(
    val email: String,
    val gender: String,
    val id_emp: Int,
    val isadmin: Boolean? = null,
    val mobile: String,
    val name: String,
    val password: String? = null,
    val uid: String
)