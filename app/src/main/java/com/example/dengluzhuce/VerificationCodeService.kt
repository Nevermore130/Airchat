package com.example.dengluzhuce
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VerificationCodeService {
    @GET("airchat/sendVerificationCode")
    fun getVerificationCode(@Query("mobile") mobile:String, @Query("type") type:Int):Call<VerificationCode>
}