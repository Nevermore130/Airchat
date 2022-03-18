package com.example.dengluzhuce

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View

import androidx.annotation.CallSuper

import kotlinx.android.synthetic.main.activity_num_validation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Num_validation : BaseActivity() {
    private var phoneNumber:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_num_validation)

        val thisActivity = this
        clearButton.setOnClickListener {
            inputText.setText("")
        }
        inputText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }


                @Suppress("DEPRECATION")
                override fun afterTextChanged(s: Editable) {
                    //这里的方法才能正常触发
                    if (s.length == 0) {
                        clearButton.visibility = View.GONE
                    }
                    if (s.length != 0) {
                        clearButton.visibility = View.VISIBLE
                    }
                    if (s.length == 11) {
                        getValidCode.setBackgroundResource(R.drawable.button_selector)
                        getValidCode.setTextColor(resources.getColor(R.color.white))

                        getValidCode.setOnClickListener {
                            phoneNumber = inputText.text.toString()
                            val intent = Intent(thisActivity, ValidCode::class.java)
                            val phoneNum = inputText.text.toString()

                            intent.putExtra("phoneNum",phoneNum)
                            startActivity(intent)
                        }
                    }
                    if (s.length != 11) {
                        getValidCode.setBackgroundResource(R.drawable.button_shape2)
                        getValidCode.setTextColor(resources.getColor(R.color.getValidCode))
                        getValidCode.isClickable = false

                    }
                }
            }
        )
        back_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}


