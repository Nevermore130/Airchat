package com.example.dengluzhuce

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_num_validation.*
import java.util.*

class Num_validation : BaseActivity() {
    private var phoneNumber:String?=null
    private val thisActivity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_num_validation)

        showKeyboard(inputText)
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

                            intent.putExtra("phoneNum", phoneNum)
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
private fun showKeyboard(view: View){
    view.requestFocus()
    val timer = Timer()
    timer.schedule(
        object : TimerTask() {
            override fun run() {
                val inputManager = view.getContext()
                    .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(view, 0)
            }
        },
        300
    )
}
}



