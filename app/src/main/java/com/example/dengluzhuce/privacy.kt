package com.example.dengluzhuce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_privacy.*

class privacy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)
        webView.settings.javaScriptEnabled=true
        webView.webViewClient= WebViewClient()
        webView.loadUrl("file:///android_asset/privacy.html")
    }
}
