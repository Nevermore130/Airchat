package com.example.dengluzhuce

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetOssTokenService {
    @GET("airchat/getOSSToken")
    fun getOssToken(@Header("cookie")cookie: String): Call<GetOssTokenReturn>
}