package com.example.dengluzhuce

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import java.sql.RowId

class UserAdapter(val userList : List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val userName : TextView = view.findViewById(R.id.userName)
        val userImage: ShapeableImageView = view.findViewById(R.id.userImage)
        val userLabel : LinearLayout = view.findViewById(R.id.userLabel)
        val userLifePhoto : LinearLayout = view.findViewById(R.id.user_lifePhoto)
        val status : TextView = view.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.visitor_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
    
    override fun getItemCount()=userList.size
}