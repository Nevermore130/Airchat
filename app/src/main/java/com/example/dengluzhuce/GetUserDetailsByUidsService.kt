package com.example.dengluzhuce

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetUserDetailsByUidsService {
    @GET("airchat/getUserDetailsByUids")
    fun getUserDetailByUids(
        @Header("cookie")cookie: String,@Query("uids")uids:String):Call<GetUserDetailsByUidsReturn>

}