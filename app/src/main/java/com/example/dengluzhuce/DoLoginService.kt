package com.example.dengluzhuce
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DoLoginService{

    @GET("sso/doLogin")
    fun doLogin(@Query("mobile") mobile:String, @Query("code") code:String,@Query("type") type:Int):Call<VerificationCode>
}