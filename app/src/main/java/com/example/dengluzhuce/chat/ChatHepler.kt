package com.example.dengluzhuce.chat

import android.content.Context
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.easeui.EaseIM
import com.hyphenate.push.EMPushConfig

class ChatHepler {

    companion object {
        val mInstance: ChatHepler = ChatHepler()
    }

    var isSDKInit = false //SDK是否初始化


    fun init(context: Context) {
        if (initSDK(context)) {
            // debug mode, you'd better set it to false, if you want release your App officially.
            EMClient.getInstance().setDebugMode(true)
        }
    }


    private fun initSDK(context: Context): Boolean {
        // 根据项目需求对SDK进行配置
        val options: EMOptions = initChatOptions(context)
        // 初始化SDK
        isSDKInit = EaseIM.getInstance().init(context, options)
        return isSDKInit
    }

    private fun initChatOptions(context: Context): EMOptions {
        val options = EMOptions()
        // 设置是否自动接受加好友邀请,默认是true
        options.acceptInvitationAlways = false
        // 设置是否需要接受方已读确认
        options.requireAck = true
        // 设置是否需要接受方送达确认,默认false
        options.requireDeliveryAck = false
        /**
         * NOTE:你需要设置自己申请的账号来使用三方推送功能，详见集成文档
         */
        val builder = EMPushConfig.Builder(context)
        builder.enableVivoPush() // 需要在AndroidManifest.xml中配置appId和appKey
            .enableMeiZuPush("134952", "f00e7e8499a549e09731a60a4da399e3")
            .enableMiPush("2882303761517426801", "5381742660801")
            .enableOppoPush(
                "0bb597c5e9234f3ab9f821adbeceecdb",
                "cd93056d03e1418eaa6c3faf10fd7537"
            )
            .enableHWPush() // 需要在AndroidManifest.xml中配置appId
            .enableFCM("782795210914")
        options.pushConfig = builder.build()
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载
        options.autoTransferMessageAttachments = true
        // 是否自动下载缩略图，默认是true为自动下载
        options.setAutoDownloadThumbnail(true)
        return options
    }


}