package com.example.dengluzhuce


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


interface GetUserInformationService {
    @GET("airchat/getUserDetail")
    fun getUserInformation(@Header("cookie")cookie: String): Call<GetUserInformationReturn>
}