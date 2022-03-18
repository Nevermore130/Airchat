package com.example.dengluzhuce

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
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
class UserName : BaseActivity() {
    private var mPermissionsChecker: PermissionChecker? = null
    private val permissionCamera = Manifest.permission.CAMERA
    private val PERMISSION_REQUEST_CODE = 0
    private var isRequireCheck: Boolean = true
    private var permissionListTmp = arrayOf(permissionCamera)
    private val activity = this

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
            mPermissionsChecker = PermissionChecker(activity)
            if(!mPermissionsChecker!!.lacksPermissions(permissionListTmp[0]))
            {
                val intent = Intent(activity, Label::class.java)
                startActivity(intent)
            }
            else {
                // 缺少权限时, 进入权限配置页面
                if (isRequireCheck) {
                    requestPermissions(
                        activity,
                        permissionListTmp,
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

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(activity, Label::class.java)
            startActivity(intent)
        }
        else{
            isRequireCheck = false

        }
    }
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(activity)
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

    @CallSuper
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (KeyboardsUtils.isShouldHideKeyBord(view, ev)) {
                if (view != null) {
                    KeyboardsUtils.hintKeyBoards(view)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
