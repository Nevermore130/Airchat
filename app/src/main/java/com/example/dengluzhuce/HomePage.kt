package com.example.dengluzhuce

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dengluzhuce.fragments.ChatFragment
import com.example.dengluzhuce.fragments.DailyFragment
import com.example.dengluzhuce.fragments.DetectFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.custom_toast.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomePage : BaseActivity() {
    private val thisActivity = this
    private var rb_chat: RadioButton? = null
    private var rb_detect: RadioButton? = null
    private var rb_daily: RadioButton? = null
    private var rg_group: RadioGroup? = null
    private var fragments: MutableList<Fragment> = arrayListOf()
    private var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserdetails()
        setContentView(R.layout.activity_home_page)
        rb_chat = findViewById(R.id.rb_chat)
        rb_detect = findViewById(R.id.rb_detect)
        rb_daily = findViewById(R.id.rb_daily)

        initRadioBtn()

        rg_group = findViewById(R.id.rg_group);
        rb_chat!!.setSelected(true);
        rg_group!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->

            if (i == R.id.rb_chat) {
                val fragmenFrom = fragments.get(position)
                position = 0
                val fragmentTo = fragments.get(position)
                swithFragmrnt(fragmenFrom,fragmentTo)
                setSelected()
                rb_chat!!.setSelected(true);

            } else if (i == R.id.rb_detect) {
                val fragmenFrom = fragments.get(position)
                position = 1
                val fragmentTo = fragments.get(position)
                swithFragmrnt(fragmenFrom,fragmentTo)
                setSelected()

                rb_detect!!.setSelected(true);
            } else {
                val fragmenFrom = fragments.get(position)
                position = 2
                val fragmentTo = fragments.get(position)
                swithFragmrnt(fragmenFrom,fragmentTo)
                setSelected()
                rb_daily!!.setSelected(true);
            }

        })

        //初始化fragment
        initFragment()

        //默认布局，选第一个
        defaultFragment()


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initRadioBtn() {

    }

    private fun initFragment() {

        fragments.add(ChatFragment())
        fragments.add(DetectFragment())
        fragments.add(DailyFragment())
    }

    private fun defaultFragment() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_layout, fragments.get(0))
        transaction.commit()
    }
private fun swithFragmrnt(fragmenFrom: Fragment,fragmentTo: Fragment){
    val fragmentManager: FragmentManager = supportFragmentManager
    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
    if(fragmenFrom!=fragmentTo){
        if(!fragmentTo.isAdded){
            transaction.hide(fragmenFrom)
                .add(R.id.fragment_layout,fragmentTo)
                .show(fragmentTo)
                .commit()
        }
        else{
            transaction.hide(fragmenFrom)
                .show(fragmentTo)
                .commit()
        }
    }

}
    private fun setSelected() {
        rb_chat!!.setSelected(false)
        rb_detect!!.setSelected(false)
        rb_daily!!.setSelected(false)

    }
    private fun getUserdetails(){
        val prefs = getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val sessionId = prefs?.getString("sessionId", "")
        if (sessionId != ""){
            val getUserInformationService = ServiceCreator.create(GetUserInformationService::class.java)
            getUserInformationService.getUserInformation(sessionId!!).enqueue(object :
                Callback<GetUserInformationReturn> {
                override fun onResponse(
                    call: Call<GetUserInformationReturn>,
                    response: Response<GetUserInformationReturn>
                ) {

                    val returnData = response.body()
                    if(returnData!=null && returnData.msg=="success")
                    {
                        Log.d("11111",returnData.data.toString())
                        val data = returnData.data.getAsJsonObject("detail")
                        val uid = data.asJsonObject.get("uid")
                        val userName = data.asJsonObject.get("nickname")
                        val userImage = data.asJsonObject.get("headimg")

                        val bundle : Bundle = Bundle()
                        bundle.putString("userImage",userImage.toString().replace("\"","")  )
                        bundle.putString("userName",userName.toString().replace("\"",""))
                        val fragment = ChatFragment()
                        fragment.arguments=bundle

                    }
                }

                override fun onFailure(call: Call<GetUserInformationReturn>, t: Throwable) {

                    t.printStackTrace()
                    Log.d("failure","+++++++++++++++++++++")
                }
            })
        }
        else{
            msgToast("登录状态异常")
        }
    }
    private fun msgToast(msg:String){

        val layout = layoutInflater.inflate(R.layout.custom_toast,linearLayout)
        val myToast= Toast.makeText(thisActivity,msg, Toast.LENGTH_LONG)
        layout.findViewById<TextView>(R.id.custom_toast_message).text=msg
        myToast.setGravity(Gravity.TOP,0, 200)

        myToast.view = layout//setting the view of custom toast layout

        myToast.show()
    }


}

