package com.example.dengluzhuce

import android.content.Context
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.widget.EditText
import android.view.MotionEvent
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService


object KeyboardsUtils {

    /**
     * 显示软键盘
     * @param view
     */
    fun showKeyboard(view: View) {
        val imm = view.getContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null) {
            view.requestFocus()
            imm!!.showSoftInput(view, 0)
        }
    }

    /**
     * 隐藏软键盘
     * @param view
     */
    fun hintKeyBoards(view: View) {
        val manager = view.getContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (manager != null) {
            manager!!.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    /**
     * 判定当前是否需要隐藏
     */
    fun isShouldHideKeyBord(v: View?, ev: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v!!.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v!!.getHeight()
            val right = left + v!!.getWidth()
            return !(ev.x > left && ev.x < right && ev.y > top && ev.y < bottom)
        }
        return false
    }
}