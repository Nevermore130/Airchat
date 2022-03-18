package com.example.dengluzhuce

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.apply {

            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or  View.SYSTEM_UI_FLAG_IMMERSIVE
        }
    }
    @CallSuper
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (KeyboardsUtils.isShouldHideKeyBord(view, ev)) {
                if (view != null) {
                    KeyboardsUtils.hintKeyBoards(view)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}