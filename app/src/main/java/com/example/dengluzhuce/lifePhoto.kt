package com.example.dengluzhuce

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.airchat.matisse.AirChatPhoto
import com.airchat.matisse.AirPhotoSelectListener
import kotlinx.android.synthetic.main.activity_life_photo.*
import kotlinx.android.synthetic.main.activity_num_validation.*
import kotlinx.android.synthetic.main.activity_num_validation.back_button

class lifePhoto : BaseActivity() {
    private val thisActivity = this
    private var mPermissionsChecker: PermissionChecker? = null
    private val permissionCamera = Manifest.permission.CAMERA
    private val permissionRead = Manifest.permission.READ_EXTERNAL_STORAGE
    private val permissionWrite = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val PermissionList = arrayOf(permissionCamera,permissionRead,permissionWrite)
    private var isRequireCheck: Boolean = true
    private val PERMISSION_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_photo)


        back_button.setOnClickListener {
            val intent = Intent(thisActivity,Label::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        mPermissionsChecker = PermissionChecker(thisActivity)
        if(!mPermissionsChecker!!.lacksPermissions(PermissionList))
        {

            imageSelect.setOnClickListener{
                AirChatPhoto.selector(thisActivity,false,object :AirPhotoSelectListener{
                    override fun onSelected(uriList: MutableList<Uri>, pathList: MutableList<String>) {
                    }

                })
            }

        }

        else {
            // 缺少权限时, 进入权限配置页面
            if (isRequireCheck) {
                ActivityCompat.requestPermissions(
                    thisActivity,
                    PermissionList,
                    PERMISSION_REQUEST_CODE
                )


            } else {
                showMissingPermissionDialog()
            }
        }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE && allPermissionOk(grantResults)) {
            imageSelect.setOnClickListener{
                AirChatPhoto.selector(thisActivity,false,object :AirPhotoSelectListener{
                    override fun onSelected(uriList: MutableList<Uri>, pathList: MutableList<String>) {
                    }

                })
            }
        }
        else{
            isRequireCheck = false

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
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(thisActivity)
        builder.setTitle("提醒")
        builder.setMessage("网络权限缺失,请前往设置页面中设置")
        // 拒绝, 退出应用
        builder.setNegativeButton("退出") { _, _ ->

            finish()
        }
        builder.setPositiveButton("设置") { _, _ -> }
        builder.show()
    }
    private fun allPermissionOk(permissionResiultList:IntArray):Boolean{
        for(permissionResult in permissionResiultList){
            if (permissionResult != PackageManager.PERMISSION_GRANTED){ return false }
        }
        return true
    }

}
