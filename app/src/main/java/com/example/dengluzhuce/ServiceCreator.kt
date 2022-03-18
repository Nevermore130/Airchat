package com.example.dengluzhuce

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL  = "https://pre-airchat.box-get.com/"
    private val retrofit =Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    fun <T> create(serviceClass : Class<T>):T = retrofit.create(serviceClass)
}