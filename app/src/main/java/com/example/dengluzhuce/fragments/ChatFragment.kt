package com.example.dengluzhuce.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.dengluzhuce.*
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.domain.EaseUser
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment
import com.hyphenate.easeui.modules.conversation.model.EaseConversationInfo
import com.hyphenate.easeui.provider.EaseUserProfileProvider
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resumeWithException

class ChatFragment : EaseConversationListFragment() {
    companion object{
        private  const val TAG: String = "ChatFragment"
    }

    private var user:MutableList<String> = arrayListOf()
    private val  mScope = MainScope()
    private val setimageAndtext = 1
    private val handle = object :Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                1->{
                    val uimage = msg.data.getString("userImage")
                    val uName = msg.data.getString("userName")
                    userName.text=uName
                    Glide.with(mContext)
                        .load(uimage)
                        .centerCrop()
                        .into(userImage)
                }
            }
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setImageAndNickName()
////        getUserdetailsByUid("339026116587536")
//        mScope.launch {
//            val data = getUserdetailsByUid("339026116587536")
//
//            msgToast("${data[1]}")
//        }

        Log.d("1111",user.toString())
    }
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //添加搜索会话布局
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.fragment_chat, null)
        llRoot.addView(view, 0)
        conversationListLayout.listAdapter.emptyLayoutId = R.layout.ease_layout_default_no_data

        EaseIM.getInstance().userProvider = EaseUserProfileProvider {
            //TODO 在这里通过userId 去获取我们自己用的信息，然后赋值给EaseUser对象
            mScope.launch {
                val data = getUserdetailsByUid(it)
                val easeUser = EaseUser()

                easeUser.avatar = "" //头像
                easeUser.nickname = "" //昵称
                easeUser
                return@launch easeUser
            }

//            }


        }
    }

    override fun initData() {
        if (EMClient.getInstance().chatManager().allConversations.isEmpty()) {
            fetchFromServer(conversationListLayout::setData) { error, errMsg ->
                Log.e(TAG, "error: $error; errMsg: $errMsg")
            }
        } else {
            super.initData()
        }
    }

    private fun fetchFromServer(
        onSuccess: (MutableList<EaseConversationInfo>) -> Unit,
        onError: (Int, String) -> Unit
    ) {
        EMClient.getInstance().chatManager().asyncFetchConversationsFromServer(object :
            EMValueCallBack<Map<String?, EMConversation>> {
            override fun onSuccess(value: Map<String?, EMConversation>) {
                val conversations: List<EMConversation> = ArrayList(value.values)
                val infoList: MutableList<EaseConversationInfo> = ArrayList()
                if (!conversations.isEmpty()) {
                    var info: EaseConversationInfo? = null
                    for (conversation in conversations) {
                        info = EaseConversationInfo()
                        info.info = conversation
                        info.timestamp = conversation.lastMessage.msgTime
                        infoList.add(info)
                    }
                }
                onSuccess.invoke(infoList)
            }

            override fun onError(error: Int, errorMsg: String) {
                onError.invoke(error, errorMsg)
            }
        })

    }
     private suspend fun getUserdetailsByUid(uid:String) :MutableList<String> {


            val prefs = activity?.getSharedPreferences("cookie", Context.MODE_PRIVATE)
            val sessionId = prefs?.getString("sessionId", "")
            if (!sessionId.isNullOrEmpty()) {

                val getUserInformationService =
                    ServiceCreator.create(GetUserDetailsByUidsService::class.java)


                val call = getUserInformationService
                    .getUserDetailByUids(cookie = sessionId, uids = uid)
                val response = awaitResponse(call)
                    val returnData = response.body()
                if (returnData != null && returnData.msg == "success") {

                                val data = returnData.data.getAsJsonArray("details")[0]

                                val userName = data.asJsonObject.get("nickname").toString()
                                val userImage = data.asJsonObject.get("headimg").toString()
                                val user  = mutableListOf<String>()
                                user.add(userImage)
                                user.add(userName)

                                return user
                            }
                return mutableListOf()



//                    .enqueue(object : Callback<GetUserDetailsByUidsReturn> {
//                        override fun onResponse(
//                            call: Call<GetUserDetailsByUidsReturn>,
//                            response: Response<GetUserDetailsByUidsReturn>
//                        ) {
//                            val returnData = response.body()
//
//                            if (returnData != null && returnData.msg == "success") {
//                                Log.d("11111", returnData.data.toString())
//                                val data = returnData.data.getAsJsonArray("details")[0]
//                                Log.d("22222", data.toString())
//                                val userName = data.asJsonObject.get("nickname").toString()
//                                val userImage = data.asJsonObject.get("headimg").toString()
//                                user.add(userImage)
//                                user.add(userName)
//
//                                return user
//                            }
//                        }
//
//                        override fun onFailure(
//                            call: Call<GetUserDetailsByUidsReturn>,
//                            t: Throwable
//                        ) {
//                            t.printStackTrace()
//                            msgToast("消息获取失败")
//                        }
//                    }

//                    )

            }

        return mutableListOf()
        }



    private fun msgToast(msg:String){

        val layout = layoutInflater.inflate(R.layout.custom_toast,linearLayout)
        val myToast= Toast.makeText(activity,msg, Toast.LENGTH_LONG)
        layout.findViewById<TextView>(R.id.custom_toast_message).text=msg
        myToast.setGravity(Gravity.TOP,0, 200)

        myToast.view = layout//setting the view of custom toast layout

        myToast.show()
    }
    private fun setImageAndNickName(){
        val prefs = activity?.getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val sessionId = prefs?.getString("sessionId", "")
        if (sessionId != ""){
            val getUserInformationService = ServiceCreator.create(GetUserInformationService::class.java)
            getUserInformationService.getUserInformation(sessionId!!).enqueue(object :
                Callback<GetUserInformationReturn> {
                override fun onResponse(
                    call: Call<GetUserInformationReturn>,
                    response: Response<GetUserInformationReturn>
                ) {

                    val returnData = response.body()
                    if(returnData!=null && returnData.msg=="success")
                    {
                        Log.d("11111",returnData.data.toString())
                        val data = returnData.data.getAsJsonObject("detail")
                        val uid = data.asJsonObject.get("uid")
                        val userName = data.asJsonObject.get("nickname")
                        val userImage = data.asJsonObject.get("headimg")

                        val bundle : Bundle = Bundle()
                        bundle.putString("userImage",userImage.toString().replace("\"","")  )
                        bundle.putString("userName",userName.toString().replace("\"",""))
                        val msg = Message()
                        msg.what=setimageAndtext
                        msg.data=bundle
                        handle.sendMessage(msg)


                    }
                }

                override fun onFailure(call: Call<GetUserInformationReturn>, t: Throwable) {

                    t.printStackTrace()
                    Log.d("failure","+++++++++++++++++++++")
                }
            })
        }
        else{
            msgToast("登录状态异常")
        }
    }
    private suspend fun awaitResponse(call: Call<GetUserDetailsByUidsReturn>): Response<GetUserDetailsByUidsReturn> {

        return suspendCancellableCoroutine {

            call.enqueue(object : Callback<GetUserDetailsByUidsReturn> {
                override fun onResponse(
                    call: Call<GetUserDetailsByUidsReturn>,
                    response: Response<GetUserDetailsByUidsReturn>
                ) {
                    it.resume(response,{})
                }

                override fun onFailure(call: Call<GetUserDetailsByUidsReturn>, t: Throwable) {
                    it.resumeWithException(t)
                }

            })
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mScope.cancel()
    }
}