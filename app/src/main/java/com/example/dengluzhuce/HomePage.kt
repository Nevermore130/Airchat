package com.example.dengluzhuce

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
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

        setContentView(R.layout.activity_home_page)
        rb_chat = findViewById(R.id.rb_chat)
        rb_detect = findViewById(R.id.rb_detect)
        rb_daily = findViewById(R.id.rb_daily)

        initRadioBtn()

        rg_group = findViewById(R.id.rg_group);
        rb_chat!!.setSelected(true);
        rg_group!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val fragmentManager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            if (i == R.id.rb_chat) {
                position = 0
                transaction.replace(R.id.fragment_layout, fragments.get(position))
                setSelected()
                rb_chat!!.setSelected(true);

            } else if (i == R.id.rb_detect) {
                position = 1
                transaction.replace(R.id.fragment_layout, fragments.get(position))
                setSelected()
                rb_detect!!.setSelected(true);
            } else {
                position = 2
                transaction.replace(R.id.fragment_layout, fragments.get(position))
                setSelected()
                rb_daily!!.setSelected(true);
            }
            transaction.commit()
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

    private fun setSelected() {
        rb_chat!!.setSelected(false)
        rb_detect!!.setSelected(false)
        rb_daily!!.setSelected(false)

    }


}

