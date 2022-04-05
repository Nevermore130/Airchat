package com.example.dengluzhuce

import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler
import android.os.Message

interface GetDetails{
    fun onDetails(details: MutableList<String>)
}
interface CallBack{
    fun onSuccess(details: MutableList<String>)
}
class OssUnit(private val sessionId : String) {
    private var details :MutableList<String>?= arrayListOf()
    fun getOssToken(getDetails: GetDetails){

        val getOssTokenService = ServiceCreator.create(GetOssTokenService::class.java)
        getOssTokenService.getOssToken(sessionId).enqueue(object :Callback<GetOssTokenReturn>{
            override fun onResponse(
                call: Call<GetOssTokenReturn>,
                response: Response<GetOssTokenReturn>
            ) {
                val returnData=response.body()
                if(returnData!=null && returnData.msg=="success"){

                    val StatusCode= returnData.data.get("StatusCode")
                        .toString().replace("\"","")
                    if(StatusCode=="200"){
                        details!!.add(returnData.data.get("AccessKeyId")
                            .toString().replace("\"",""))
                        details!!.add(returnData.data.get("AccessKeySecret")
                            .toString().replace("\"",""))
                        details!!.add(returnData.data.get("SecurityToken")
                            .toString().replace("\"",""))

                        getDetails!!.onDetails(details!!)
                    }

                }
            }

            override fun onFailure(call: Call<GetOssTokenReturn>, t: Throwable) {
                t.printStackTrace()
                details!!.add("null")

            }
        })


        }
}