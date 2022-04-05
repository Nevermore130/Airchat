package com.example.dengluzhuce

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.airchat.matisse.AirChatPhoto
import com.airchat.matisse.AirPhotoSelectListener
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import kotlinx.android.synthetic.main.activity_home_page.*
import kotlinx.android.synthetic.main.custom_toast.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.LongFunction


class HomePage : BaseActivity() {
    private val thisActivity=this
    private val updateData = 1
    private var mdetails : MutableList<String>?= arrayListOf()
    private val endPoint = "oss-cn-beijing.aliyuncs.com"
    private val bucketName = "box-get-public-photos"
    private val key = "airchat"

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
                    Log.d("test2222222","22222")
                    val intent = Intent(thisActivity,MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<LogoutReturn>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("test111111111","111111")
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