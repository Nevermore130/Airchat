package com.example.dengluzhuce

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface LogoutService {
    @GET("sso/logout")
    fun logout(@Header("cookie")cookie: String): Call<LogoutReturn>
}