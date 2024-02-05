package com.example.pointage_application.network.api

import com.example.pointage_application.models.Details
import com.example.pointage_application.models.Token
import com.example.pointage_application.models.UserRequest
import com.example.pointage_application.models.UserResponse
import com.example.pointage_application.models.UserUpdate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface APIAdmin {

    @FormUrlEncoded
    @POST("/admins/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Token>

    @GET("/admins/current")
    suspend fun currentAdmin(@Header("authorization") token: String): Response<UserResponse>

    @POST("/admins/signup")
    suspend fun signup(@Body adminData: UserRequest): Response<UserResponse>

    @PUT("/admins")
    suspend fun updateAdmin(@Body adminUpdate: UserUpdate): Response<UserResponse>


    @FormUrlEncoded
    @PUT("/admins/password/{id_}")
    suspend fun updatePasswordAdmin(@Path("id_") id_: Int, @Field("old_password") old_password: String, @Field("new_password") new_password: String ): Response<UserResponse>

    @DELETE("/admins")
    suspend fun deleteAdmin(@Field("password") idAdmin: Int): Response<Details>
}