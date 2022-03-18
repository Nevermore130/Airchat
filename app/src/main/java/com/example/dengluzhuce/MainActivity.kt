package com.example.dengluzhuce

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowInsets
//import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.telephony.TelephonyManager
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.text.underline
import com.device.id.DeviceIdUtils


class MainActivity : BaseActivity() {

    private val activity = this
    private var mPermissionsChecker: PermissionChecker? = null
    private val permissionInternet = Manifest.permission.INTERNET
    private val PERMISSION_REQUEST_CODE = 0
    private var isRequireCheck: Boolean = true
    private var permissionListTmp = arrayOf(permissionInternet)
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val IMEL=DeviceIdUtils.getDeviceId(this@MainActivity,useMd5 = false)

            Toast.makeText(this, "IMEI number: " + IMEL,
                Toast.LENGTH_LONG).show()


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
                    ds.isUnderlineText=false
                    agreeItem.highlightColor=resources.getColor(R.color.transparent)
                }

            },
            agreeItem.text.length - 13,
            agreeItem.text.length - 7,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spanable.setSpan(object : ClickableSpan() {

            override fun onClick(v: View) {

                val intent = Intent(activity,privacy::class.java)
                startActivity(intent)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText=false
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

        buttonOfLogin.setOnClickListener {

            mPermissionsChecker = PermissionChecker(activity)
            if(!mPermissionsChecker!!.lacksPermissions(permissionListTmp[0]))
            {

//                val intent = Intent(activity, Num_validation::class.java)
                val intent = Intent(activity, lifePhoto::class.java)
                startActivity(intent)
            }
            else {
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
            }
        else{
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