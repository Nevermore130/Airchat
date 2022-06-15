package com.example.dengluzhuce.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.example.dengluzhuce.R
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.domain.EaseUser
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment
import com.hyphenate.easeui.modules.conversation.model.EaseConversationInfo
import com.hyphenate.easeui.provider.EaseUserProfileProvider

class ChatFragment : EaseConversationListFragment() {
    private val TAG: String = "ChatFragment"

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //添加搜索会话布局
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.fragment_chat, null)
        llRoot.addView(view, 0)
        conversationListLayout.listAdapter.emptyLayoutId = R.layout.ease_layout_default_no_data
        EaseIM.getInstance().userProvider = EaseUserProfileProvider {
            //TODO 在这里通过userId 去获取我们自己用的信息，然后赋值给EaseUser对象

            val easeUser = EaseUser()
            easeUser.avatar = "" //头像
            easeUser.nickname = "" //昵称
            easeUser
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


}