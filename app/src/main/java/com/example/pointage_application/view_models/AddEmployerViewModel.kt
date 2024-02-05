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
import com.example.pointage_application.models.UserRequest
import com.example.pointage_application.network.RetrofitInstance
import kotlinx.coroutines.launch

class AddEmployerViewModel(application: Application): AndroidViewModel(application) {


    var email by mutableStateOf("")
    var mobile by mutableStateOf("")
    var uid by mutableStateOf("")
    var name by mutableStateOf("")
    var gender by mutableStateOf("")

    val isAdded = MutableLiveData(false)





    // plate code
    val token = "Bearer ${SessionManagements(application).getToken()}"


    private val isLoading_ = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = isLoading_

    private val isError_: MutableLiveData<ErrorMessage?> = MutableLiveData(null)
    val isError: LiveData<ErrorMessage?> = isError_


    var isThereIsAdd = false

    private val services = RetrofitInstance.getInstance().getEmployerInstance()


    // end plate code

    fun addEmployer() {
        isLoading_.postValue(true)
        if (email.isEmpty() || uid.isEmpty() || mobile.isEmpty() || gender.isEmpty()) {
            isError_.postValue(ErrorMessage("Empty Filed", "You Have To Fill Your Data Correctly"))
            isLoading_.postValue(false)
            return;
        }


        viewModelScope.launch {
            val response = services.addEmployer(
                UserRequest(
                    email = email,
                    mobile = mobile,
                    uid = uid,
                    gender = gender,
                    name = name,
                ),
                token
            )

            if (response.body() != null && response.code() == 200) {
                isAdded.postValue(true)
            }else if (response.code() == 401){
                isError_.postValue(ErrorMessage("Access Deny", "You May Need To Resign in, Your Request Not Allowed"))
            }else if (response.code() == 409){
                isError_.postValue(ErrorMessage("Identifier Exists", "There Identifier either Mail Or uid exists"))
            }
            else {
                isError_.postValue(ErrorMessage("Error ${response.code()}", "Error Difficile To See, Call Your Administrator"))
            }

            isLoading_.postValue(false)

        }

    }



    fun resetError() {
        isError_.postValue(null)
    }
    fun resetAdded() {
        isAdded.postValue(null)
    }


    fun resetAll() {
        email = ""
        uid = ""
        gender = ""
        name = ""
        mobile = ""
    }
}