package com.example.dengluzhuce

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showStatusBar()
    }

    open fun showStatusBar() {
        if (isStatusBarTranslucent()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.0 以上直接设置状态栏颜色
                val decorView = window.decorView
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                decorView.systemUiVisibility = option
                window.statusBarColor = Color.TRANSPARENT
            }
        } else {
            ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarDarkIcon(true)
                .navigationBarColor(R.color.white)
                .init()
        }
    }

    open fun isStatusBarTranslucent(): Boolean {
        return false
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