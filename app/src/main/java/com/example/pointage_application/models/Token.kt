package com.example.pointage_application.models

data class Token(
    val access_token: String,
    val exp: String,
    val token_type: String
)