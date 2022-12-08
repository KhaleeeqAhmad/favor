package com.fyp.favorproject.utill

import com.fyp.favorproject.model.Chats
import com.fyp.favorproject.model.User

interface OnChatStart {

    fun onChatStart(friend: User,currentUser: User, chats: ArrayList<Chats>)
}