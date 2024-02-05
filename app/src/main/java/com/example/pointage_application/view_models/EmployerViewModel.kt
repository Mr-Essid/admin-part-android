package com.example.pointage_application.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pointage_application.SessionManagements
import com.example.pointage_application.models.ErrorMessage
import com.example.pointage_application.models.HistoryResponse
import com.example.pointage_application.models.UserResponse
import com.example.pointage_application.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response

class EmployerViewModel(application: Application) : AndroidViewModel(application) {

    val token = "Bearer ${SessionManagements(application).getToken()}"

    private val employerList_ = MutableLiveData<List<UserResponse>?>(null)
    val employerList: LiveData<List<UserResponse>?> = employerList_

    private val isLoading_ = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoading_

    private val isError_: MutableLiveData<ErrorMessage?> = MutableLiveData(null)
    val isError: LiveData<ErrorMessage?> = isError_

    private val services = RetrofitInstance.getInstance().getEmployerInstance()

    init {
        loadEmployersFromTheServer()
    }


        fun loadEmployersFromTheServer() {
            isLoading_.postValue(true)
            viewModelScope.launch {
                val response= services.getEmployers(token)

                if (response.body() != null && response.code() == 200) {
                    employerList_.postValue(response.body())

                }else if (response.code() == 401){
                    isError_.postValue(ErrorMessage("Access Deny", "You May Need To Resign in, Your Request Not Allowed"))
                }else
                    isError_.postValue(ErrorMessage("Error ${response.code()}", "Error Difficile To See, Call Your Administrator"))

                isLoading_.postValue(false)
            }

        }


    fun resetError() {
        isError_.postValue(null)
    }


}

