package com.example.dengluzhuce

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.airchat.matisse.AirChatPhoto
import com.airchat.matisse.AirPhotoSelectListener
import kotlinx.android.synthetic.main.activity_life_photo.*
import kotlinx.android.synthetic.main.activity_num_validation.*
import kotlinx.android.synthetic.main.activity_num_validation.back_button

class lifePhoto : BaseActivity() {
private val thisActivity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_photo)
        back_button.setOnClickListener {
            val intent = Intent(this,Label::class.java)
            startActivity(intent)
        }
    imageSelect.setOnClickListener{
        AirChatPhoto.selector(thisActivity,false,object :AirPhotoSelectListener{
            override fun onSelected(uriList: MutableList<Uri>, pathList: MutableList<String>) {
            }

        })
    }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            23->if(resultCode==RESULT_OK){
                val uris : List<Uri> = AirChatPhoto.obtainResult(data!!)
            }
        }
    }

}
