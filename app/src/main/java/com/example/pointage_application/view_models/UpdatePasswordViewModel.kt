package com.example.pointage_application.view_models

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pointage_application.SessionManagements
import com.example.pointage_application.models.ErrorMessage
import com.example.pointage_application.models.UserResponse
import com.example.pointage_application.models.UserUpdate
import com.example.pointage_application.network.RetrofitInstance
import kotlinx.coroutines.launch

class UpdatePasswordViewModel(application: Application) : AndroidViewModel(application){



    var oldPassword by mutableStateOf("")
    var newPassword by mutableStateOf("")
    var conFirmPassword by mutableStateOf("")



    // plate code
    val token = "Bearer ${SessionManagements(application).getToken()}"


    private val isLoading_ = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = isLoading_

    private val isError_: MutableLiveData<ErrorMessage?> = MutableLiveData(null)
    val isError: LiveData<ErrorMessage?> = isError_

    private val isUpdated_: MutableLiveData<Boolean> = MutableLiveData(false)
    val isUpdated: LiveData<Boolean> = isUpdated_

    private val services = RetrofitInstance.getInstance().getAdminInstance()



    // end plate code

    fun userUpdate(id_: Int, old_password: String) {
        isLoading_.postValue(true)
        if (newPassword.isEmpty() || oldPassword.isEmpty() || conFirmPassword.isEmpty()) {
            isError_.postValue(ErrorMessage("Empty Filed", "You Have To Fill Your Data Correctly"))
            emptyValues()
            isLoading_.postValue(false)
            return;
        }

        if (newPassword  != conFirmPassword) {
            isError_.postValue(ErrorMessage("Confirm Error", "Incorrect Confirmation"))
            emptyValues()
            isLoading_.postValue(false)
            return;
        }




        viewModelScope.launch {


            val response = services.updatePasswordAdmin(id_, oldPassword, newPassword)

            if (response.body() != null && response.code() == 200) {
                isUpdated_.postValue(true)

            }else if (response.code() == 404){
                isError_.postValue(ErrorMessage("Access Deny", "Your Not The Man Character Here!"))
                emptyValues()
            }else if (response.code() == 400){
                isError_.postValue(ErrorMessage("Password Incorrect", "Password Incorrect"))
                emptyValues()
            }
            else {
                isError_.postValue(ErrorMessage("Error ${response.code()}", "Error Difficile To See, Call Your Administrator"))
                emptyValues()
            }

            isLoading_.postValue(false)

        }

    }




    fun resetError() {
        isError_.postValue(null)
    }

    fun resetUpdate() {
        isUpdated_.postValue(false)
    }

    fun emptyValues() {
        oldPassword = ""
        newPassword = ""
        conFirmPassword = ""
    }
}