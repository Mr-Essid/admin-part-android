package com.example.pointage_application.network.api

import com.example.pointage_application.models.UserRequest
import com.example.pointage_application.models.UserResponse
import com.example.pointage_application.models.UserUpdate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface APIEmployer {


    @POST("/employers/employer")
    suspend fun addEmployer(@Body employerRequest: UserRequest, @Header("Authorization") token: String): Response<UserResponse>

    @GET("/employers")
    suspend fun getEmployers(@Header("Authorization") token: String): Response<List<UserResponse>>

    @GET("/employers/employer")
    suspend fun getEmployerBy(
        @Query("id_") id_: Int? = null,
        @Query("email") email: String? = null,
        @Query("uid") uid: String? = null,
        @Header("Authorization") token: String
    ): Response<UserResponse>

    @PUT("/employers/employer")
    suspend fun updateEmployer(@Body employerUpdate: UserUpdate, @Header("Authorization") token: String): Response<UserResponse>

    @DELETE("/employers/employer")
    suspend fun deleteEmployer(@Query("id_") id_: Int, @Header("Authorization") token: String)


}