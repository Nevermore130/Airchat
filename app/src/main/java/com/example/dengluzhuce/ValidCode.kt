package com.example.dengluzhuce

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.CallSuper
import kotlinx.android.synthetic.main.activity_valid_code.*
import android.view.Gravity
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_num_validation.*
import kotlinx.android.synthetic.main.activity_valid_code.back_button
import kotlinx.android.synthetic.main.custom_toast.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class ValidCode : BaseActivity() {
    private var timer: TimerUnit? = null
    private val thisActivity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_valid_code)

        val mContext = thisActivity.baseContext



        val phoneNumber  = intent.getStringExtra("phoneNum").toString()

        val verificationCodeService = ServiceCreator.create(VerificationCodeService::class.java)
        verificationCodeService.getVerificationCode(phoneNumber,1).enqueue(object :Callback<VerificationCode>{
            override fun onResponse(
                call: Call<VerificationCode>,
                response: Response<VerificationCode>
            ) {

                msgToast(resources.getString(R.string.validCodeSendSuccess))
            }

            override fun onFailure(
                call: Call<VerificationCode>,
                t: Throwable
            ) {
                msgToast(resources.getString(R.string.validCodeSendFailure))
                t.printStackTrace()
            }
        })
        val editText : EditText =findViewById(R.id.validCode1)
        showKeyboard(editText)
        var userInput:IntArray= intArrayOf(-1,-1,-1,-1)
        back_button.setOnClickListener {

            val intent = Intent(thisActivity,Num_validation::class.java)
            startActivity(intent)
        }



        text_phoneNum.append(phoneNumber)

        if (timer == null) {

            timer = TimerUnit(reGetValidCode,mContext)
        }
        timer?.startTime()

        reGetValidCode.setOnClickListener {
            timer = TimerUnit(reGetValidCode,mContext)
            timer?.startTime()
            verificationCodeService.getVerificationCode(phoneNumber,1).enqueue(object :Callback<VerificationCode>{
                override fun onResponse(
                    call: Call<VerificationCode>,
                    response: Response<VerificationCode>
                ) {

                    msgToast(resources.getString(R.string.validCodeSendSuccess))
                }

                override fun onFailure(
                    call: Call<VerificationCode>,
                    t: Throwable
                ) {
                    msgToast(resources.getString(R.string.validCodeSendFailure))
                    t.printStackTrace()
                }
            })
            showKeyboard(editText)
        }





        validCode1.addTextChangedListener(
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
                        if (s.length==1){
                            validCode2.isFocusable=true
//                            validCode2.focusable=View.FOCUSABLE
                            validCode2.isFocusableInTouchMode=true
                            validCode2.requestFocus()
                            validCode2.isCursorVisible=true
                            validCode1.isFocusable=false
//                            validCode1.focusable=View.NOT_FOCUSABLE
                            validCode1.isCursorVisible=false
                            val value = (validCode1.text).toString().toInt()
                            userInput[0]=value
                        }
                    }
                }
                )




        validCode2.addTextChangedListener(
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
                    if (s.length==1){
                        validCode3.isFocusable=true
//                        validCode3.focusable=View.FOCUSABLE
                        validCode3.isFocusableInTouchMode=true
                        validCode3.isCursorVisible=true
                        validCode3.requestFocus()
                        validCode2.isFocusable=false
//                        validCode2.focusable=View.NOT_FOCUSABLE
                        validCode2.isCursorVisible=false
                        val value = (validCode2.text).toString().toInt()
                        userInput[1]=value
                    }
                }
            }
        )


        validCode3.addTextChangedListener(
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
                    if (s.length==1){
                        validCode4.isFocusable=true
                        validCode4.isFocusableInTouchMode=true
                        validCode4.isCursorVisible=true
                        validCode4.requestFocus()
                        validCode3.isFocusable=false
                        validCode3.isCursorVisible=false
                        val value = (validCode3.text).toString().toInt()
                        userInput[2]=value
                    }
                }
            }
        )
    validCode2.setOnKeyListener(object : View.OnKeyListener {
        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

            if (keyCode == KeyEvent.KEYCODE_DEL) {
                validCode1.setText("")
                validCode1.isFocusable=true
                validCode1.isFocusableInTouchMode=true
                validCode1.isCursorVisible=true
                validCode2.isFocusable=false
                validCode2.isCursorVisible=false
                validCode1.requestFocus()

            }
            return false
        }
    })
    validCode3.setOnKeyListener(object : View.OnKeyListener {
        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

            if (keyCode == KeyEvent.KEYCODE_DEL) {
                validCode1.setText("")
                validCode2.setText("")
                validCode1.isFocusable=true
                validCode1.isFocusableInTouchMode=true
                validCode1.isCursorVisible=true
                validCode2.isFocusable=false
                validCode2.isCursorVisible=false
                validCode3.isFocusable=false
                validCode3.isCursorVisible=false
                validCode1.requestFocus()

            }
            return false
        }
    })
        validCode4.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    validCode1.setText("")
                    validCode2.setText("")
                    validCode3.setText("")
                    validCode4.setText("")
                    validCode1.isFocusable=true
                    validCode1.isFocusableInTouchMode=true
                    validCode1.isCursorVisible=true
                    validCode2.isFocusable=false
                    validCode3.isFocusable=false
                    validCode4.isFocusable=false
                    validCode2.isCursorVisible=false
                    validCode3.isCursorVisible=false
                    validCode4.isCursorVisible=false
                    validCode1.requestFocus()

                }
                return false
            }
        })
        validCode4.addTextChangedListener(
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
                    if (s.length==1){
                        reGetValidCode.visibility=View.INVISIBLE
                        progressBar.visibility=View.VISIBLE

                        val value = (validCode4.text).toString().toInt()
                        userInput[3]=value

                        val userinput:String ="${userInput[0]}${userInput[1]}${userInput[2]}${userInput[3]}"


                        val doLogin = ServiceCreator.create(DoLoginService::class.java)
                        doLogin.doLogin(phoneNumber,userinput,1).enqueue(object : Callback<VerificationCode> {
                            override fun onResponse(
                                call: Call<VerificationCode>,
                                response: Response<VerificationCode>
                            ) {
                                Log.d("+++++++++++++++++","成功获取响应")
                                val cookie=SessionUtilTools.getSession(response.headers())
                                Log.d("test_cookie",cookie)
                                val editor = getSharedPreferences("cookie",Context.MODE_PRIVATE).edit()
                                editor.putString("sessionId",cookie)
                                editor.apply()

                                val returnData = response.body()
                                if(returnData!=null && returnData.msg=="success" ){
                                        if(returnData!!.data.get("needPerfectDetail").toString() =="false") {

                                            val intent = Intent(thisActivity,HomePage::class.java)
                                            startActivity(intent)
                                        }else{
                                            val intent = Intent(thisActivity, Sex::class.java)
                                            startActivity(intent)
                                        }

                                    finish()
//


                                }

                            }

                            override fun onFailure(call: Call<VerificationCode>, t: Throwable) {
                                t.printStackTrace()
                                msgToast(resources.getString(R.string.validCodeError))
                            }

                        })



                    }

                }
            }
        )


        back_button.setOnClickListener {
            val intent = Intent(this, Num_validation::class.java)
            startActivity(intent)
        }




    }
    private fun msgToast(msg:String){

        val layout = layoutInflater.inflate(R.layout.custom_toast,linearLayout)
        val myToast=Toast.makeText(thisActivity,msg,Toast.LENGTH_LONG)
        layout.findViewById<TextView>(R.id.custom_toast_message).text=msg
        myToast.setGravity(Gravity.TOP,0, 200)

        myToast.view = layout//setting the view of custom toast layout

        myToast.show()

        validCode1.setText("")
        validCode2.setText("")
        validCode3.setText("")
        validCode4.setText("")
        validCode1.isFocusable=true
        validCode1.isFocusableInTouchMode=true
        validCode2.isFocusable=false
        validCode3.isFocusable=false
        validCode4.isFocusable=false
        validCode1.requestFocus()
        validCode1.isCursorVisible=true
        validCode2.isCursorVisible=false
        validCode3.isCursorVisible=false
        validCode4.isCursorVisible=false
        progressBar.visibility=View.INVISIBLE
        reGetValidCode.visibility=View.VISIBLE
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
