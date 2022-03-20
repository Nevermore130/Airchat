package com.example.dengluzhuce

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_label.*
import kotlinx.android.synthetic.main.activity_num_validation.clearButton
import kotlinx.android.synthetic.main.activity_num_validation.inputText
import kotlinx.android.synthetic.main.activity_user_name.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import androidx.core.app.ActivityCompat.requestPermissions
import com.airchat.matisse.AirChatPhoto
import com.airchat.matisse.AirPhotoSelectListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_life_photo.*

class UserName : BaseActivity() {
    private var mPermissionsChecker: PermissionChecker? = null
    private val permissionCamera = Manifest.permission.CAMERA
    private val permissionRead = Manifest.permission.READ_EXTERNAL_STORAGE
    private val permissionWrite = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val PermissionList = arrayOf(permissionCamera, permissionRead, permissionWrite)
    private val PERMISSION_REQUEST_CODE = 0
    private var isRequireCheck: Boolean = true

    private val thisActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_name)
        val userSex:String = intent.getStringExtra("userSex").toString()
        clearButton.setOnClickListener {
            inputText.setText("")
        }//限制表情的输入
        inputText.setFilters(arrayOf<InputFilter>(stringFilter(),InputFilter.LengthFilter(10)))
        //限制表情的输入//限制最大输入字符

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

                //                @SuppressLint("ResourceAsColor")
                @Suppress("DEPRECATION")
                override fun afterTextChanged(s: Editable) {
                    //这里的方法才能正常触发
                    if (s.length == 0) {
                        clearButton.visibility = View.GONE
                        buttonOfUserName.isEnabled=false
                        buttonOfUserName.background=resources.getDrawable(R.drawable.button_shape2)
                        buttonOfUserName.setTextColor(resources.getColor(R.color.gray_text))
                    }
                    if (s.length != 0) {
                        clearButton.visibility = View.VISIBLE
                        buttonOfUserName.background=resources.getDrawable(R.drawable.button_selector)
                        buttonOfUserName.isEnabled=true
                        buttonOfUserName.setTextColor(resources.getColor(R.color.white))
                    }

                }
            }
        )

    }

    override fun onResume() {
        super.onResume()

        buttonOfUserName.setOnClickListener {
            mPermissionsChecker = PermissionChecker(thisActivity)
            if (!mPermissionsChecker!!.lacksPermissions(PermissionList)) {


                AirChatPhoto.selector(
                    thisActivity,
                    1,
                    object : AirPhotoSelectListener {
                        override fun onSelected(
                            uriList: MutableList<Uri>,
                            pathList: MutableList<String>
                        ) {
                        }

                    })


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

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE && allPermissionOk(grantResults)) {

            buttonOfUserName.setOnClickListener {
                AirChatPhoto.selector(
                    thisActivity,
                    1,
                    object : AirPhotoSelectListener {
                        override fun onSelected(
                            uriList: MutableList<Uri>,
                            pathList: MutableList<String>
                        ) {
                        }

                    })
            }

        }
        else{
            isRequireCheck = false

        }
    }
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(thisActivity)
        builder.setTitle("提醒")
        builder.setMessage("相机权限缺失,请前往设置页面中设置")
        // 拒绝, 退出应用
        builder.setNegativeButton("退出") { _, _ ->

            finish()
        }
        builder.setPositiveButton("设置") { _, _ -> }
        builder.show()
    }
    inner class stringFilter : InputFilter {
        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): String? {

            val regEx : String= "[^a-zA-Z0-9\u4E00-\u9FA5]"
            val p : Pattern= Pattern.compile(regEx)
            val m : Matcher= p.matcher(source)
            return m.replaceAll("").trim().toString()


    }

        }
    private fun allPermissionOk(permissionResultList: IntArray): Boolean {
        for (permissionResult in permissionResultList) {
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            23 -> if (resultCode == RESULT_OK) {
                val uris: List<Uri> = AirChatPhoto.obtainResult(data!!)
                val intent = Intent(thisActivity, Label::class.java)
                intent.putExtra("imageUri",uris[0].toString())
                Log.d("uriiiiiii",uris[0].toString())
                startActivity(intent)
            }
        }
    }
}
