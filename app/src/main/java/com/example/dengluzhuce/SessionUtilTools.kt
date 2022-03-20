package com.example.dengluzhuce


import android.util.Log
import okhttp3.Headers


object SessionUtilTools {
    fun getSession(headers: Headers): String {
        val cookies = headers.values("Set-Cookie")
        val session = cookies[0]
        Log.d("session",session.toString())
        return session.substring(0, session.indexOf(";"))
    }
}