package com.example.dengluzhuce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_sex.*

class Sex : BaseActivity() {
    private var userSex:String?=null
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sex)
        girl.requestFocus()
        nextTip.isEnabled=false
        checkbox.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup,i->
            text2.setTextColor(resources.getColor(R.color.sex_check))
            nextTip.isEnabled=true
            nextTip.background=resources.getDrawable(R.drawable.button_selector)
            nextTip.setTextColor(resources.getColor(R.color.white))
            val checkedButton = radioGroup.findViewById(i) as RadioButton
            if(checkedButton.text.toString()=="我是男生"){userSex="boy"}
            else{userSex="girl"}

        })

        nextTip.setOnClickListener {
            val intent = Intent(this,UserName::class.java)
            Log.d("userSex",userSex!!)
            intent.putExtra("userSex",userSex)
            startActivity(intent)
        }

        }

    }

