package com.example.dengluzhuce

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UpdateUserDetailsService {
    @GET("airchat/updateUserDetail")
    fun updateUserDetails(@Query("nickname")nickname: String,@Query("headimg")headimg:String,
                          @Query("gender")gender:Int,@Query("tagIds")tagIds:String,
                          @Query("lifepics")lifepics:String,@Header("cookie")cookie:String
    ): Call<UpdateUserDetailsReturn>
    @GET("airchat/updateUserDetail")
    fun updateUserDetailsWithNoTagIds(@Query("nickname")nickname: String,@Query("headimg")headimg:String,
                          @Query("gender")gender:Int,
                          @Query("lifepics")lifepics:String,@Header("cookie")cookie:String
    ): Call<UpdateUserDetailsReturn>
    @GET("airchat/updateUserDetail")
    fun updateUserDetailsWithNoLifepics(@Query("nickname")nickname: String,@Query("headimg")headimg:String,
                          @Query("gender")gender:Int,@Query("tagIds")tagIds:String,
                          @Header("cookie")cookie:String
    ): Call<UpdateUserDetailsReturn>
    @GET("airchat/updateUserDetail")
    fun updateUserDetailsAllNone(@Query("nickname")nickname: String,@Query("headimg")headimg:String,
                          @Query("gender")gender:Int,@Header("cookie")cookie:String
    ): Call<UpdateUserDetailsReturn>
}