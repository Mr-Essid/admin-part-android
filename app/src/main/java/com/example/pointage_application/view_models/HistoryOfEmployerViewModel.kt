package com.example.pointage_application.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pointage_application.SessionManagements
import com.example.pointage_application.models.ErrorMessage
import com.example.pointage_application.models.HistoryEmployerResponse
import com.example.pointage_application.network.RetrofitInstance
import kotlinx.coroutines.launch

class HistoryOfEmployerViewModel(application: Application): AndroidViewModel(application) {

    val token = "Bearer ${SessionManagements(application).getToken()}"


    private val historyList_ = MutableLiveData<Map<String, List<HistoryEmployerResponse>?>>(null)
    val historyList: LiveData<Map<String, List<HistoryEmployerResponse>?>> = historyList_

    private val isLoading_ = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = isLoading_

    private val isError_: MutableLiveData<ErrorMessage?> = MutableLiveData(null)
    val isError: LiveData<ErrorMessage?> = isError_

    private val services = RetrofitInstance.getInstance().getHistoryInstance()


    fun loadHistoryFromTheServer(id: Int) {
        isLoading_.postValue(true)
        viewModelScope.launch {

            val response = services.getHistoryOfEmployer(id, token)

            if (response.body() != null && response.code() == 200) {
                val res = groupingListOfElements(response.body()!!)
                historyList_.postValue(res)
            } else if (response.code() == 401) {
                isError_.postValue(
                    ErrorMessage(
                        "Access Deny",
                        "You May Need To Resign in, Your Request Not Allowed"
                    )
                )
            } else
                isError_.postValue(
                    ErrorMessage(
                        "Error ${response.code()}",
                        "Error Difficile To See, Call Your Administrator"
                    )
                )

            isLoading_.postValue(false)
        }


    }

    private fun groupingListOfElements(listEmployerHistory: List<HistoryEmployerResponse>): Map<String, MutableList<HistoryEmployerResponse>> {

        val simpleMap = mutableMapOf<String, MutableList<HistoryEmployerResponse>>()
        listEmployerHistory.stream().forEach {
            val monthYear = it.date.subSequence(0, it.date.lastIndexOf('-')).toString()
            if (!simpleMap.containsKey(monthYear)) {
                simpleMap[monthYear] = mutableListOf()
            }
            simpleMap[monthYear]!!.add(it)
        }

        return simpleMap
    }


}