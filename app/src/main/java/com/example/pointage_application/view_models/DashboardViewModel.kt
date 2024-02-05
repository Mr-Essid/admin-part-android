package com.example.pointage_application.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pointage_application.SessionManagements
import com.example.pointage_application.models.ErrorMessage
import com.example.pointage_application.models.UserResponse
import com.example.pointage_application.network.RetrofitInstance
import kotlinx.coroutines.launch


class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    val currentAdmin = MutableLiveData<UserResponse>(null)

    private val sessionManagements = SessionManagements(application)
    private val token = "Bearer ${SessionManagements(application).getToken()}"
    val expTimeAsString = SessionManagements(application).getExpTimeAsString()

    val isLoading_ = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoading_

    private val adminService = RetrofitInstance.getInstance().getAdminInstance()
    val error : MutableLiveData<ErrorMessage> = MutableLiveData(null)

    init {
        loadCurrentAdmin()
    }

    private fun loadCurrentAdmin() {
        isLoading_.postValue(true)
        viewModelScope.launch {
            val response = adminService.currentAdmin(token)
            if (response.body() != null && response.code() == 200) {
                currentAdmin.postValue(response.body())
            }else if (response.code() == 401) {
                error.postValue(ErrorMessage("Access Deny", "Request Not Permitted"))
            }else
                error.postValue(ErrorMessage(response.code().toString(), "call your administrator"))
            isLoading_.postValue(false)
        }
    }


    fun removeAllData() {
        sessionManagements.removeToken()
    }



}