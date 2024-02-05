package com.example.pointage_application


import android.app.Application
import android.content.Context

class SessionManagements(application: Application) {
    val TOKEN_NAME = "TOKEN_NAME__"
    val sharedPreferences = application.getSharedPreferences("TOKEN", Context.MODE_PRIVATE)


    fun addToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(TOKEN_NAME, token)
            // token will add with form TOKEN_EXP_TIME
            apply()
        }

    }

    fun getToken() : String{
        val stringToken = sharedPreferences.getString(TOKEN_NAME, "")
        if (stringToken!!.isEmpty()) {
            throw IllegalArgumentException("There is No Token!")
        }
        return stringToken.split("_T_")[0]
    }

    fun getExpTimeAsString(): String {


        val stringToken = sharedPreferences.getString(TOKEN_NAME, "")
        if (stringToken!!.isEmpty()) {
            throw IllegalArgumentException("There is No Token!")
        }
        return stringToken.split("_T_")[1]
    }

    fun removeToken(): Boolean {
        with(sharedPreferences.edit()) {
            remove(TOKEN_NAME)
            apply()
        }
        return true
    }

}