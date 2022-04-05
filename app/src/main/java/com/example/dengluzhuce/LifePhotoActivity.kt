package com.example.dengluzhuce

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airchat.matisse.AirChatPhoto
import com.airchat.matisse.AirPhotoSelectListener
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_life_photo.*
import kotlinx.android.synthetic.main.activity_num_validation.back_button
import kotlinx.android.synthetic.main.activity_valid_code.*
import kotlinx.android.synthetic.main.custom_toast.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class LifePhotoActivity : BaseActivity() {
    private val endPoint = "oss-cn-beijing.aliyuncs.com"
    private val bucketName = "box-get-public-photos"
    private val key = "airchat"
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
    private var uriList : MutableList<Uri> = arrayListOf()
    private var bundle =Bundle()
    private var fileNameList:MutableList<String>?= arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundle = intent.getBundleExtra("bundle")!!
        setContentView(R.layout.activity_life_photo)
        val spanable = SpannableStringBuilder(skip.text)
        skip.setMovementMethod(LinkMovementMethod.getInstance())
        spanable.setSpan(
            object : ClickableSpan() {

                override fun onClick(v: View) {
                    progressbar.visibility=View.VISIBLE
                    bundle.putString("lifepics","")
                    uriList.add(Uri.parse(bundle.getString("uri")))
                    buttonOfLogin.isEnabled=false
                    getOssToken(object :CallBack{
                        override fun onSuccess(details: MutableList<String>) {
                            ossUpload(uriList,details)
                        }
                    })
                    updateUserDetails()
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
                var flag = uris.size
                uris.forEach {
                    uriList.add(it)
                    Log.d("test_uriList",uriList.toString())
                    Glide.with(thisActivity)
                        .load(it)
                        .centerCrop()
                        .into(imageSelectList!![index])
                    if(!buttonOfLogin.isEnabled) {
                        buttonOfLogin.isEnabled = true
                        buttonOfLogin.background = resources.getDrawable(R.drawable.button_shape)
                        buttonOfLogin.setTextColor(resources.getColor(R.color.black))
                        buttonOfLogin.setOnClickListener {
                            uriList.add(Uri.parse(bundle.getString("uri")))
                            progressbar.visibility=View.VISIBLE
                            buttonOfLogin.isEnabled=false
                            getOssToken(object :CallBack{
                                override fun onSuccess(details: MutableList<String>) {
                                    ossUpload(uriList,details)
                                }
                            })

                        }
                    }

                    imageSelectList!![index].isEnabled=false
                    index+=1
                    flag -=1
                    if(index<3){
                        imageSelectList!![index].visibility=View.VISIBLE
                        if(flag==0){
                        imageSelectList!![index].setOnClickListener {

                            AirChatPhoto.selector(
                                thisActivity,
                                3 - index,
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
    }
    private fun ossUpload(uris: List<Uri>,details:MutableList<String>){
        var num=uriList.size
        val credentialProvider = OSSStsTokenCredentialProvider(details[0], details[1], details[2])
        val oss = OSSClient(applicationContext, endPoint, credentialProvider)
        uriList.forEach{

            val objectKey=getObjectKey(it)

            if (num>2){
                fileNameList!!.add(getFileName(objectKey))
                upLoadOss(oss, objectKey, it,object : ossUploadListener{
                    override fun onFailure(boolean: Boolean) {
                        msgToast("出现错误！")
                    }

                    override fun onSuccess(boolean: Boolean) {
                        Log.d("test_it",it.toString())
                    }
                })}
            else if(num==2){
                fileNameList!!.add(getFileName(objectKey))
                bundle.putString("lifepics",getLifePictures(fileNameList!!))
                Log.d("test_bundle",bundle.toString())
                upLoadOss(oss, objectKey, it,object : ossUploadListener{
                    override fun onFailure(boolean: Boolean) {
                        msgToast("出现错误！")
                    }

                    override fun onSuccess(boolean: Boolean) {
                        Log.d("test_uriList",uris.toString())
                    }
                })
            }
            else{
                bundle.putString("headimg",getFileName(objectKey))
                Log.d("test_bundle",bundle.toString())
                upLoadOss(oss, objectKey, it,object : ossUploadListener{
                    override fun onFailure(boolean: Boolean) {
                        msgToast("出现错误！")
                    }

                    override fun onSuccess(boolean: Boolean) {
                        Log.d("test_uriList",uris.toString())
                        updateUserDetails()

                    }
                })
            }
            num-=1
        }
    }
    private fun updateUserDetails(){
        val updateUserDetailsService = ServiceCreator.create(UpdateUserDetailsService::class.java)
            updateUserDetailsService.updateUserDetails(
                cookie = getCookie(),
                nickname = bundle.getString("nickname")!!,
                headimg = bundle.getString("headimg")!!,
                gender = bundle.getInt("gender"),
                tagIds = bundle.getString("tagIds")!!,
                lifepics = bundle.getString("lifepics")!!).enqueue(object :Callback<UpdateUserDetailsReturn>{
            override fun onResponse(
                call: Call<UpdateUserDetailsReturn>,
                response: Response<UpdateUserDetailsReturn>
            ) {
                val returnData = response.body()
                Log.d("test_response",returnData!!.msg)
                if(returnData!=null && returnData.msg=="success")
                {
                    val intent = Intent(thisActivity,HomePage::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                }

            }

            override fun onFailure(
                call: Call<UpdateUserDetailsReturn>,
                t: Throwable
            ) {
                msgToast("信息上传失败")
            }
        })
    }
    private fun getOssToken(callBack: CallBack){
        val prefs = getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val sessionId = prefs.getString("sessionId", "")
        val mOssUnit = OssUnit(sessionId!!)

        mOssUnit!!.getOssToken(object :GetDetails{
            override fun onDetails(details: MutableList<String>) {

                callBack.onSuccess(details)

            }
        })


    }
    private fun msgToast(msg:String){

        val layout = layoutInflater.inflate(R.layout.custom_toast,linearLayout)
        val myToast= Toast.makeText(thisActivity,msg, Toast.LENGTH_LONG)
        layout.findViewById<TextView>(R.id.custom_toast_message).text=msg
        myToast.setGravity(Gravity.TOP,0, 200)

        myToast.view = layout//setting the view of custom toast layout

        myToast.show()

        validCode1.setText("")
        validCode2.setText("")
        validCode3.setText("")
        validCode4.setText("")
        validCode1.isFocusable=true
        validCode1.isFocusableInTouchMode=true
        validCode2.isFocusable=false
        validCode3.isFocusable=false
        validCode4.isFocusable=false
        validCode1.requestFocus()
        validCode1.isCursorVisible=true
        validCode2.isCursorVisible=false
        validCode3.isCursorVisible=false
        validCode4.isCursorVisible=false
        progressBar.visibility=View.INVISIBLE
        reGetValidCode.visibility=View.VISIBLE
    }
    private fun getObjectKey(uris: Uri):String{
        val opt = BitmapFactory.Options()
        opt.inJustDecodeBounds=true
        BitmapFactory.decodeStream(contentResolver.openInputStream(uris),null,opt)
        val imageWidth = opt.outWidth
        val imageHeight = opt.outHeight
        val suffix = opt.outMimeType.split("/")[1]
        val md5=Md5util.getMD5(contentResolver.openInputStream(uris))
        val fileName = "${md5}_w${imageWidth}_h${imageHeight}.${suffix}"
        val objectKey = "${key}/${fileName}"
        return objectKey
    }
    private fun upLoadOss(oss: OSSClient, objectKey:String, uris: Uri, ossUploadListener: ossUploadListener){
        val put = PutObjectRequest(bucketName, objectKey,uris)
        val task: OSSAsyncTask<*> = oss.asyncPutObject(put, object :
            OSSCompletedCallback<PutObjectRequest?, PutObjectResult> {
            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult) {
                Log.d("PutObject", "UploadSuccess")
                Log.d("ETag", result.eTag)
                Log.d("RequestId", result.requestId)
                ossUploadListener.onSuccess(true)
                //走到这里就是上传完成了
                //上传成功后的url不会在这里返回。可以去控制台查看一下，前面的域名是OSS配置的域名，后面直接拼接objectKey即可，是一个普通的地址，可以直接访问和下载
            }

            override fun onFailure(request: PutObjectRequest?, clientExcepion: ClientException?, serviceException: ServiceException?) {
                //走到这里就是报错了
                clientExcepion?.printStackTrace()
                if (serviceException != null) {
                    // 服务异常
                    ossUploadListener.onFailure(false)
                    Log.d("ErrorCode", serviceException.errorCode)
                    Log.d("RequestId", serviceException.requestId)
                    Log.d("HostId", serviceException.hostId)
                    Log.d("RawMessage", serviceException.rawMessage)
                }
            }
        })
        task.waitUntilFinished()
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
    private fun getFileName(objectKey: String):String{
        return objectKey.split("/")[1]

    }
    private fun getLifePictures(fileNameList: MutableList<String>):String{
        return  fileNameList.toString().replace("[","").replace("]","").replace(" ","")
    }
    private fun getCookie():String{
        val prefs = getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val sessionId = prefs.getString("sessionId", "")
        return sessionId!!
    }

}
