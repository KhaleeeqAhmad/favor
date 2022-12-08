package com.fyp.favorproject.utill

import com.fyp.favorproject.model.Chats
import com.fyp.favorproject.model.User

interface ChatsCallBack {

    fun onChatsFetched(chats:ArrayList<Chats>)
    fun onFriendsMetaDataFetched(friend: User)
}