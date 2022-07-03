package com.example.dengluzhuce

import android.app.ActionBar
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.android.synthetic.main.activity_visitor.*

class VisitorActivity : AppCompatActivity() {
    private val thisActivity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitor)
        val relativeLayout:RelativeLayout = findViewById(R.id.user_lifePhoto)



        val textlist: List<String> = listOf("大二学霸","小个子","微胖yyds")
            for (i in 0 until 3 ){

                val iv: ShapeableImageView = LayoutInflater.from(thisActivity).inflate(
                    R.layout.user_life_photo,
                    relativeLayout,
                    false
                ) as ShapeableImageView
                iv.setImageResource(R.drawable.boy)
                val lp=iv.layoutParams as RelativeLayout.LayoutParams
                lp.setMargins(0,dp2px(5),dp2px((i+1)*8+i*30),dp2px(5))
                lp.width=dp2px(30)
                lp.height=dp2px(30)
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
                iv.layoutParams=lp
                relativeLayout.addView(iv)
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