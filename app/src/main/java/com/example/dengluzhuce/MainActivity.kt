package com.example.dengluzhuce

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.device.id.DeviceIdUtils
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : BaseActivity() {

    private val activity = this
    private var mPermissionsChecker: PermissionChecker? = null
    private val permissionInternet = Manifest.permission.INTERNET
    private val PERMISSION_REQUEST_CODE = 0
    private var isRequireCheck: Boolean = true
    private var permissionListTmp = arrayOf(permissionInternet)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val IMEL = DeviceIdUtils.getDeviceId(this@MainActivity, useMd5 = false)
        Toast.makeText(
            this, "IMEI number: " + IMEL,
            Toast.LENGTH_LONG
        ).show()
        val spanable = SpannableStringBuilder(agreeItem.text)
        agreeItem.setMovementMethod(LinkMovementMethod.getInstance())

        spanable.setSpan(
            object : ClickableSpan() {

                override fun onClick(v: View) {
                    val intent = Intent(activity, protocol::class.java)
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    agreeItem.highlightColor = resources.getColor(R.color.transparent)
                }

            },
            agreeItem.text.length - 13,
            agreeItem.text.length - 7,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spanable.setSpan(object : ClickableSpan() {

            override fun onClick(v: View) {

                val intent = Intent(activity, privacy::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }

        }, agreeItem.text.length - 6, agreeItem.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        spanable.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    agreeItem.context,
                    R.color.useragree
                )

            ),
            agreeItem.text.length - 13,
            agreeItem.text.length - 7,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spanable.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    agreeItem.context,
                    R.color.useragree

                )
            ), agreeItem.text.length - 6, agreeItem.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        agreeItem.setText(spanable)

    }

    override fun onResume() {
        super.onResume()
        buttonOfLogin.isClickable=true
        buttonOfLogin.setOnClickListener {
            buttonOfLogin.isClickable=false
            mPermissionsChecker = PermissionChecker(activity)
            if (!mPermissionsChecker!!.lacksPermissions(permissionListTmp)) {

//                val intent = Intent(activity, Num_validation::class.java)
                val intent = Intent(activity, HomePage::class.java)
                startActivity(intent)
            } else {
                // 缺少权限时, 进入权限配置页面
                if (isRequireCheck) {
                    requestPermissions(activity, permissionListTmp, PERMISSION_REQUEST_CODE)


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
            val intent = Intent(activity, Num_validation::class.java)
            startActivity(intent)
        } else {
            isRequireCheck = false

        }
    }

    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("提醒")
        builder.setMessage("网络权限缺失,请前往设置页面中设置")
        // 拒绝, 退出应用
        builder.setNegativeButton("退出") { _, _ ->

            finish()
        }
        builder.setPositiveButton("设置") { _, _ -> }
        builder.show()
    }


}




//}
