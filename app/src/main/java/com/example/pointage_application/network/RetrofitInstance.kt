package com.example.pointage_application.network

import com.example.pointage_application.network.api.APIAdmin
import com.example.pointage_application.network.api.APIEmployer
import com.example.pointage_application.network.api.APIHistory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit


class RetrofitInstance private constructor() {



    companion object {
        private var retrofitInstance_: Retrofit? = null
        private val BASE_URL = "http://192.168.81.97:8000"

        @Volatile
        private var retrofitInstance: RetrofitInstance? = null

        fun getInstance(baseUrl: String = BASE_URL): RetrofitInstance {
            retrofitInstance_ = Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient().newBuilder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .readTimeout(50, TimeUnit.MINUTES)
                        .callTimeout(50, TimeUnit.MINUTES)
                        .connectTimeout(50, TimeUnit.MINUTES)
                        .writeTimeout(50, TimeUnit.MINUTES)
                        .build()
                ).build()

            if (retrofitInstance == null) {
                retrofitInstance = RetrofitInstance()
            }

            return retrofitInstance!!
        }


    }

    fun getAdminInstance(): APIAdmin {
        return retrofitInstance_!!.create(APIAdmin::class.java)
    }

    fun getEmployerInstance(): APIEmployer {
        return retrofitInstance_!!.create(APIEmployer::class.java)
    }

    fun getHistoryInstance(): APIHistory {
        return retrofitInstance_!!.create(APIHistory::class.java)
    }
}