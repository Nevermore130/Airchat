package com.example.dengluzhuce

import android.app.ActionBar
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_visitor.*

class VisitorActivity : AppCompatActivity() {
    private val thisActivity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitor)
        val linearLayout:LinearLayout = findViewById(R.id.userLabel)



        val textlist: List<String> = listOf("大二学霸","小个子","微胖yyds")
            for (text in textlist){

                val tv: TextView = LayoutInflater.from(thisActivity).inflate(
                    R.layout.user_label,
                    linearLayout,
                    false
                ) as TextView
                tv.text=text
                val lp=tv.layoutParams as LinearLayout.LayoutParams
                lp.setMargins(dp2px(4),0,0,0)
                tv.layoutParams=lp
                linearLayout.addView(tv)
            }
    }
    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}