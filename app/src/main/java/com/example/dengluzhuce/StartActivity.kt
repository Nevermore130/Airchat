package com.example.dengluzhuce
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        keepLogin()
    }
    private fun keepLogin() {
        val prefs = getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val sessionId = prefs.getString("sessionId", "")
        Log.d("test_cookie",sessionId.toString())
        if (sessionId != "") {

        val getUserInformationService = ServiceCreator.create(GetUserInformationService::class.java)
            getUserInformationService.getUserInformation(sessionId!!).enqueue(object : Callback<GetUserInformationReturn> {
                    override fun onResponse(
                        call: Call<GetUserInformationReturn>,
                        response: Response<GetUserInformationReturn>
                    ) {
                        val returnData = response.body()
                        Log.d("aaaaa","aaaaaaaaaaaaaa")
                        if(returnData!=null && returnData.msg=="success")
                        {
                            val intent = Intent(this@StartActivity,HomePage::class.java)
                            startActivity(intent)

                        }
                        else{
                            Log.d("bbbbbbb","bbbbbbbbb")
                            val intent = Intent(this@StartActivity,MainActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<GetUserInformationReturn>, t: Throwable) {
                        t.printStackTrace()
                        Log.d("failure","+++++++++++++++++++++")
                        val intent = Intent(this@StartActivity,MainActivity::class.java)
                        startActivity(intent)
                    }

                })
        }
        else{
            Log.d("noCookie","1111111111111")
            val intent = Intent(this@StartActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }
}