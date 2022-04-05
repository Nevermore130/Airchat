package com.example.dengluzhuce

import android.Manifest
import android.app.usage.NetworkStats
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
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
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSS
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_life_photo.*
import kotlinx.android.synthetic.main.activity_valid_code.*
import kotlinx.android.synthetic.main.custom_toast.*
import retrofit2.http.OPTIONS
import java.util.*

class UserName : BaseActivity() {
    private var mPermissionsChecker: PermissionChecker? = null
    private val permissionCamera = Manifest.permission.CAMERA
    private val permissionRead = Manifest.permission.READ_EXTERNAL_STORAGE
    private val permissionWrite = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val PermissionList = arrayOf(permissionCamera, permissionRead, permissionWrite)
    private val PERMISSION_REQUEST_CODE = 0
    private var isRequireCheck: Boolean = true
    private val thisActivity = this
    private val endPoint = "oss-cn-beijing.aliyuncs.com"
    private val bucketName = "box-get-public-photos"
    private val key = "airchat"
    private val bundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_name)
        val userSex:Int= intent.getIntExtra("userSex",-1)
        showKeyboard(inputText)
        bundle.putInt("gender",userSex)


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
            buttonOfUserName.isEnabled=false
            mPermissionsChecker = PermissionChecker(thisActivity)
            if (!mPermissionsChecker!!.lacksPermissions(PermissionList)) {

                bundle.putString("nickname",inputText.text.toString())
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



                bundle.putString("nickname",inputText.text.toString())
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
                bundle.putString("uri",uris[0].toString())
                Log.d("test_bundle",bundle.toString())
                val intent = Intent(thisActivity,Label::class.java)
                intent.putExtra("bundle",bundle)
                startActivity(intent)




            }
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
