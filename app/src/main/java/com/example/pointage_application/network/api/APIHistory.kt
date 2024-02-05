package com.example.pointage_application.network.api

import com.example.pointage_application.models.HistoryEmployerResponse
import com.example.pointage_application.models.HistoryResponse
import com.example.pointage_application.models.MonthPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDate

interface APIHistory {
    @GET("/history")
    suspend fun getAllHistory(@Header("authorization") token: String): Response<List<HistoryResponse>>

    @GET("/history/uuid/{uuid}")
    suspend fun getHistoryWithUID(@Path("uuid") uuid: String, @Header("authorization") token: String): Response<List<HistoryResponse>>


    @GET("/history/employer/{id_}")
    suspend fun getHistoryOfEmployer(@Path("id_") id: Int, @Header("authorization") token: String): Response<List<HistoryEmployerResponse>>

    @GET("/history/date/{date_}")
    suspend fun getHistoryOfDate(@Path("date_") date_: String, @Header("authorization") token: String): Response<List<HistoryResponse>>

    @GET("/history/per-month/{id_}")
    suspend fun getHistoryPerMonthOfEmployer(@Path("/id_") id: Int, @Header("authorization") token: String): Response<MonthPoints>


}