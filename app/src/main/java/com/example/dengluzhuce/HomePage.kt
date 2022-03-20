package com.example.dengluzhuce

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.custom_toast.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePage : BaseActivity() {
    private val thisActivity=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val prefs = getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val sessionId = prefs.getString("sessionId", "")
        button1.setOnClickListener {
            val getUserInformationService = ServiceCreator.create(GetUserInformationService::class.java)
            getUserInformationService.getUserInformation(sessionId!!).enqueue(object :
                Callback<GetUserInformationReturn> {
                override fun onResponse(
                    call: Call<GetUserInformationReturn>,
                    response: Response<GetUserInformationReturn>
                ) {
                    val returnData = response.body()
                    Log.d("aaaaa","aaaaaaaaaaaaaa")
                    if(returnData!=null && returnData.msg=="success")
                    {
                        val data= returnData.data.getAsJsonObject("detail")
                        val name = data.get("nickname").toString().replace("\"","")
                        msgToast(name)

                    }
                }

                override fun onFailure(call: Call<GetUserInformationReturn>, t: Throwable) {
                    t.printStackTrace()
                    msgToast("获取名称失败")

                }

            })
        }
        button2.setOnClickListener {
            val logoutService = ServiceCreator.create(LogoutService::class.java)
            logoutService.logout(sessionId!!).enqueue(object :Callback<LogoutReturn>{
                override fun onResponse(
                    call: Call<LogoutReturn>,
                    response: Response<LogoutReturn>
                ) {
                    val intent = Intent(thisActivity,MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<LogoutReturn>, t: Throwable) {
                    t.printStackTrace()
                    msgToast("注销登录失败")
                }
            })
        }
    }
    private fun msgToast(msg:String) {

        val layout = layoutInflater.inflate(R.layout.custom_toast, linearLayout)
        val myToast = Toast.makeText(thisActivity, msg, Toast.LENGTH_SHORT)
        layout.findViewById<TextView>(R.id.custom_toast_message).text = msg
        myToast.setGravity(Gravity.TOP, 0, 200)

        myToast.view = layout//setting the view of custom toast layout

        myToast.show()
    }
}