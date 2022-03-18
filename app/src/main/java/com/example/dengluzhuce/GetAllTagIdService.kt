package com.example.dengluzhuce

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetAllTagIdService {
    @GET("airchat/getAllTag")
    fun getAllTagId(@Query("gender")gender:Int):Call<GetAllTagIdReturn>
}