package com.example.pointage_application.view_models

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pointage_application.SessionManagements
import com.example.pointage_application.models.ErrorMessage
import com.example.pointage_application.network.RetrofitInstance
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManagements = SessionManagements(application)
    private val adminAPI = RetrofitInstance.getInstance().getAdminInstance()
    var username = mutableStateOf("")
    var passowrd = mutableStateOf("")
    var error = MutableLiveData<ErrorMessage?>(null)
        private set
    val isAuthenticated = MutableLiveData(false)

    val loadingData = MutableLiveData(false)
    private val TAG = "LoginViewModel"
    

    fun login() : Int{

        loadingData.postValue(true)

        if (username.value.isEmpty() || passowrd.value.isEmpty()) {
            loadingData.postValue(false)
            return -1
        }

        viewModelScope.launch {
            val response = adminAPI.login(username.value, passowrd.value)
            loadingData.postValue(false)
            if (response.body() != null && response.code() == 200) {
                val token_ = response.body()!!.access_token
                val exp_time = response.body()!!.exp
                val finalStructureOfToken = "${token_}_T_${exp_time}"
                Log.d(TAG, "login: token $finalStructureOfToken")
                sessionManagements.addToken(finalStructureOfToken)
                isAuthenticated.postValue(true)
            }else if (response.code() == 401) {
                Log.d(TAG, "login: ${response.code()}")
                error.postValue(ErrorMessage("UNAUTHORIZED", "username or password incorrect"))
            }else {
                error.postValue(ErrorMessage(response.code().toString(), "server error call your admin please"))
            }

            Log.d(TAG, "login: ${response.code()}")

        }

        return 1
    }



}