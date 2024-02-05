package com.example.pointage_application.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pointage_application.SessionManagements
import com.example.pointage_application.models.ErrorMessage
import com.example.pointage_application.models.HistoryResponse
import com.example.pointage_application.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit

class HistoryViewModel(application: Application): AndroidViewModel(application) {

    val token = "Bearer ${SessionManagements(application).getToken()}"

    private val historyList_ = MutableLiveData<List<HistoryResponse>?>(null)
    val historyList: LiveData<List<HistoryResponse>?> = historyList_

    private val isLoading_ = MutableLiveData(false)
    val isLoading : LiveData<Boolean> = isLoading_

    private val isError_: MutableLiveData<ErrorMessage?> = MutableLiveData(null)
    val isError: LiveData<ErrorMessage?> = isError_

    private val services = RetrofitInstance.getInstance().getHistoryInstance()

    init {
        loadHistoryFromTheServer()
    }

    fun loadHistoryFromTheServer(dateOfHistory: String? = null) {
        isLoading_.postValue(true)
        viewModelScope.launch {
            var response: Response<List<HistoryResponse>>? = null
            response = if (dateOfHistory == null)
                services.getAllHistory(token)
            else
                services.getHistoryOfDate(dateOfHistory, token)

            if (response.body() != null && response.code() == 200) {
                historyList_.postValue(response.body())
            }else if (response.code() == 401){
                isError_.postValue(ErrorMessage("Access Deny", "You May Need To Resign in, Your Request Not Allowed"))
            }else
                isError_.postValue(ErrorMessage("Error ${response.code()}", "Error Difficile To See, Call Your Administrator"))

            isLoading_.postValue(false)
        }



    }






}