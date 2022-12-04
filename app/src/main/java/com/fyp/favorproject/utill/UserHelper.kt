package com.fyp.favorproject.utill

import android.content.Intent
import android.util.Log
import com.fyp.favorproject.mainFragment.ChattingActivity
import com.fyp.favorproject.mainFragment.HomeFragment
import com.fyp.favorproject.model.Chats
import com.fyp.favorproject.model.Posts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val auth = FirebaseAuth.getInstance()
private val database = FirebaseDatabase.getInstance()

class UserHelper {

    companion object {
        fun getCurrentUserChats(callBack: ChatsCallBack) {
            val currentUser = auth.currentUser
            var chats: ArrayList<Chats> = ArrayList()
            CoroutineScope(Dispatchers.IO).launch {
                database.reference.child("User").child(currentUser!!.uid).child("chats").get()
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            if (it.result.exists()) {
                                for (i in it.result.children) {
                                    val c = i.getValue(Chats::class.java)
                                    chats.add(c!!)
                                }
                            }
                        }
                        callBack.onChatsFetched(chats)
                    }
            }
        }

        fun openChat(friendUID: String?, post: Posts, context: HomeFragment) {
            database.reference.child("User").child(auth.uid!!).child("chats").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (it.result.exists()) {
                            var flag = false
                            for (i in it.result.children) {
                                var c = i.getValue(Chats::class.java)
                                if (c!!.friendUID == friendUID) {
                                    Log.d(
                                        "UserHelper",
                                        "openChat: Already have a chat..opening chat"
                                    )
                                    flag = true

                                    startChatActivity(context, c)
                                    break

                                }
                            }
                            if (!flag) {
                                Log.d("UserHelper", "openChat:Don't have a chat..opening chat")

                                createChatRoom(friendUID, post, context)

                            }
                        } else {
                            Log.d("UserHelper", "openChat:Don't have a chat..opening chat")

                        }

                    } else {
                        Log.d("UserHelper", "openChat: ${it.exception}")

                    }
                }


        }

        private fun createChatRoom(friendUID: String?, post: Posts, context: HomeFragment) {

            var charRoomName = auth.currentUser!!.uid + friendUID

            val chat = Chats(
                friendUID = friendUID,
                friendName = post.userNameResourceID.toString(),
                friendProfileURI = "123",
                lastMessage = "",
                lastMessageTime = "",
                createdTime = "123",
                chatUID = charRoomName
            )

            database.reference.child("User").child(auth.uid!!).child("chats").child(charRoomName)
                .setValue(chat)
                .addOnSuccessListener {
                    //now creating chat room
                    var data = mapOf("lastMsg" to "", "lastMsgTime" to "")

                    database.reference.child("Chats").child(charRoomName).setValue(data)
                        .addOnSuccessListener {
                            Log.d("UserHelper", "createChatRoom: created")
                            startChatActivity(context, chat)
                        }
                }

        }

        private fun startChatActivity(context: HomeFragment, chat: Chats) {
            var intent = Intent(context.context, ChattingActivity::class.java)
            intent.putExtra("name", chat.friendName)
            intent.putExtra("picture", chat.friendProfileURI)
            intent.putExtra("uid", chat.friendUID)
            context.requireContext().startActivity(intent)
        }

    }
}