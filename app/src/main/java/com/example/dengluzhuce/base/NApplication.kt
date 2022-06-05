package com.example.dengluzhuce.base

import android.app.Application
import android.util.Log
import com.example.dengluzhuce.chat.ChatHepler
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient

class NApplication : Application() {

    companion object {
        var instance: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        ChatHepler.mInstance.init(this)
        mockLoginUser()
    }

    private fun mockLoginUser() {
        EMClient.getInstance().login("ariChatTest", "123456", object : EMCallBack {
            override fun onSuccess() {
                Log.e("mockLoginUser", "login success")
            }

            override fun onError(code: Int, error: String?) {
                Log.e("mockLoginUser", "login fail: $error")
            }

        })

    }
}