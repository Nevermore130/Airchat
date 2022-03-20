package com.example.dengluzhuce

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airchat.matisse.AirChatPhoto
import com.airchat.matisse.AirPhotoSelectListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_label.*
import kotlinx.android.synthetic.main.activity_life_photo.*
import kotlinx.android.synthetic.main.activity_life_photo.skip
import kotlinx.android.synthetic.main.activity_num_validation.*
import kotlinx.android.synthetic.main.activity_num_validation.back_button

class LifePhotoActivity : BaseActivity() {
    private val thisActivity = this
    private var mPermissionsChecker: PermissionChecker? = null
    private val permissionCamera = Manifest.permission.CAMERA
    private val permissionRead = Manifest.permission.READ_EXTERNAL_STORAGE
    private val permissionWrite = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val PermissionList = arrayOf(permissionCamera, permissionRead, permissionWrite)
    private var isRequireCheck: Boolean = true
    private val PERMISSION_REQUEST_CODE = 1
    private var index = 0
    private var imageSelectList : MutableList<ImageView>?= arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_photo)
        val spanable = SpannableStringBuilder(skip.text)
        skip.setMovementMethod(LinkMovementMethod.getInstance())
        spanable.setSpan(
            object : ClickableSpan() {

                override fun onClick(v: View) {
                    val intent = Intent(thisActivity, HomePage::class.java)
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    skip.highlightColor = resources.getColor(R.color.transparent)
                }

            }, 0, skip.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spanable.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    skip.context,
                    R.color.gray888888
                )

            ),
            0,
            skip.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        skip.setText(spanable)
        imageSelectList!!.add(imageSelect1 )
        imageSelectList!!.add(imageSelect2 )
        imageSelectList!!.add(imageSelect3 )
        Log.d("image1",imageSelect1.toString())
        Log.d("imageSelecter",imageSelectList.toString())

        back_button.setOnClickListener {
            val intent = Intent(thisActivity, Label::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        mPermissionsChecker = PermissionChecker(thisActivity)
        if (!mPermissionsChecker!!.lacksPermissions(PermissionList)) {

            imageSelect1.setOnClickListener {
                AirChatPhoto.selector(
                    thisActivity,
                    3,
                    object : AirPhotoSelectListener {
                        override fun onSelected(
                            uriList: MutableList<Uri>,
                            pathList: MutableList<String>
                        ) {
                        }

                    })
            }

        } else {
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
            imageSelect1.setOnClickListener {
                AirChatPhoto.selector(
                    thisActivity,
                    3,
                    object : AirPhotoSelectListener {
                        override fun onSelected(
                            uriList: MutableList<Uri>,
                            pathList: MutableList<String>
                        ) {
                        }

                    })
            }
        } else {
            isRequireCheck = false

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            23 -> if (resultCode == RESULT_OK) {
                val uris: List<Uri> = AirChatPhoto.obtainResult(data!!)
                uris.forEach {
                    Glide.with(thisActivity)
                        .load(it)
                        .centerCrop()
                        .into(imageSelectList!![index])
                    if(!buttonOfLogin.isEnabled) {
                        buttonOfLogin.isEnabled = true
                        buttonOfLogin.background = resources.getDrawable(R.drawable.button_shape)
                        buttonOfLogin.setTextColor(resources.getColor(R.color.black))
                        buttonOfLogin.setOnClickListener {
                            val intent = Intent(thisActivity,HomePage::class.java)
                            startActivity(intent)
                        }
                    }

                    imageSelectList!![index].isEnabled=false
                    index+=1
                    if(index<3){
                        imageSelectList!![index].visibility=View.VISIBLE
                        imageSelectList!![index].setOnClickListener {

                            AirChatPhoto.selector(
                                thisActivity,
                                3-index,
                                object : AirPhotoSelectListener {
                                    override fun onSelected(
                                        uriList: MutableList<Uri>,
                                        pathList: MutableList<String>
                                    ) {
                                    }

                                })

                    }
                    }
                }
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

    private fun allPermissionOk(permissionResultList: IntArray): Boolean {
        for (permissionResult in permissionResultList) {
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

}
