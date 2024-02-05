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

class UpdateUserViewModel(application: Application): AndroidViewModel(application) {


    // all states

    var email by mutableStateOf("")
    var mobile by mutableStateOf("")
    var uid by mutableStateOf("")

    //  end statea


    private val currentUser_ = MutableLiveData<UserResponse>(null)
    val currentUser: LiveData<UserResponse> = currentUser_


    // plate code
    val token = "Bearer ${SessionManagements(application).getToken()}"


    private val isLoading_ = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = isLoading_

    private val isError_: MutableLiveData<ErrorMessage?> = MutableLiveData(null)
    val isError: LiveData<ErrorMessage?> = isError_

    private val isUpdated_: MutableLiveData<Boolean> = MutableLiveData(false)
    val isUpdated: LiveData<Boolean> = isUpdated_

    private val services = RetrofitInstance.getInstance().getEmployerInstance()

    var isThereIsUpdate = false

    // end plate code

    fun userUpdate() {
        isLoading_.postValue(true)
        if (email.isEmpty() || uid.isEmpty() || mobile.isEmpty()) {
            isError_.postValue(ErrorMessage("Empty Filed", "You Have To Fill Your Data Correctly"))
            email = currentUser.value!!.email
            uid = currentUser.value!!.uid
            mobile = currentUser.value!!.mobile
            isLoading_.postValue(false)
            return;
        }

        viewModelScope.launch {
            val response = services.updateEmployer(
                UserUpdate(email, currentUser.value!!.id_emp, mobile, uid),
                token
            )

            if (response.body() != null && response.code() == 200) {
                isUpdated_.postValue(true)
            }else if (response.code() == 401){
                isError_.postValue(ErrorMessage("Access Deny", "You May Need To Resign in, Your Request Not Allowed"))
                setUser(currentUser.value!!)
            }else if (response.code() == 409){
                isError_.postValue(ErrorMessage("Identifier Exists", "There Identifier either Mail Or uid exists"))
                setUser(currentUser.value!!)
            }
            else {
                isError_.postValue(ErrorMessage("Error ${response.code()}", "Error Difficile To See, Call Your Administrator"))
                setUser(currentUser.value!!)
            }

            isLoading_.postValue(false)

        }

    }


    fun setUser(userResponse: UserResponse) {
        currentUser_.postValue(userResponse)
        email = userResponse.email
        uid = userResponse.uid
        mobile = userResponse.mobile
    }


    fun resetError() {
        isError_.postValue(null)
    }

    fun resetUpdate() {
        isUpdated_.postValue(false)
    }


}