package com.example.dengluzhuce.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dengluzhuce.ContractActivity

import com.example.dengluzhuce.R
import kotlinx.android.synthetic.main.fragment_chat.*

class DailyFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily,container,false)
        contract.setOnClickListener{
            val intent = Intent(activity,ContractActivity::class.java)
            startActivity(intent)
        }
    }

}