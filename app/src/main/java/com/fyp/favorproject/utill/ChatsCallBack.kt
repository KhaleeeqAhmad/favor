package com.fyp.favorproject.utill

import com.fyp.favorproject.model.Chats

interface ChatsCallBack {

    fun onChatsFetched(chats:ArrayList<String>)
}