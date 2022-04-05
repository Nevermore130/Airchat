package com.example.dengluzhuce
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.widget.TextView

@Suppress("DEPRECATION")
class TimerUnit(private val textView: TextView,mContext :Context) : Handler() {
    private var defaultTime = 60
    private var time = defaultTime
    private var isShowEndText = true
    private var mContext = mContext
    private var timeEndListener: OnTimeEndListener? = null

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            time--
            if (time == 0) {
                endtTime()

                return
            }
            textView.text = String.format("重新获取（%ds）", time)
            postDelayed(this, 1000)
        }
    }

    fun setTimeEndListener(timeEndListener: OnTimeEndListener) {
        this.timeEndListener = timeEndListener
    }

    fun setShowEndText(showEndText: Boolean) {
        isShowEndText = showEndText
    }


    fun setTime(time: Int) {
        this.defaultTime = time
        this.time = defaultTime
    }

    fun startTime() {
        post(runnable)
        textView.isEnabled = false
        textView.setBackgroundResource(R.drawable.button_shape2)
        textView.setTextColor(mContext.resources.getColor(R.color.gray_text))
    }


    fun pauseTime() {
        removeCallbacks(runnable)
        time = defaultTime
    }


    fun endtTime() {
        if (isShowEndText) {
            textView.text = "重新获取"
            textView.setBackgroundResource(R.drawable.button_selector)
//            textView.setTextColor(getResources()!!.getColor(R.color.white))
            textView.setTextColor(mContext.resources.getColor(R.color.white))
        }
        textView.isEnabled = true
        removeCallbacks(runnable)
        time = defaultTime
        if (timeEndListener != null) {
            timeEndListener!!.timeEnd()
        }
    }

    interface OnTimeEndListener {
        fun timeEnd()
    }

}
