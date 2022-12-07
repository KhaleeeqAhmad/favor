package com.fyp.favorproject.utill

import android.content.Intent
import android.util.Log
import com.fyp.favorproject.fragments.HomeFragment
import com.fyp.favorproject.mainFragment.ChattingActivity
import com.fyp.favorproject.model.Chats
import com.fyp.favorproject.model.Posts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private val auth = FirebaseAuth.getInstance()
private val database = FirebaseDatabase.getInstance()

class UserHelper {

    companion object {
        fun getCurrentUserChats(callBack: ChatsCallBack) {


            val currentUser = auth.currentUser
            val chats: ArrayList<String> = ArrayList()

            Log.d("CCAADD", "getCurrentUserChats:called ")
            database.reference.child("User").child(currentUser!!.uid).child("chats").get().addOnCompleteListener {

                 if (it.isSuccessful){
                     Log.d("CC", "getCurrentUserChats: data fetched ")
                     if (it.result.exists()) {
                         Log.d("CCAADD", "getCurrentUserChats: result exist ")

                         for (i in it.result.children) {
                             val c = i.value.toString()
                             chats.add(c)
                         }

                     }
                     callBack.onChatsFetched(chats)

                 }
       if (it.isCanceled){
           Log.d("CCAADD", "getCurrentUserChats: result cancelled ")
           callBack.onChatsFetched(chats)
       }


       if (it.isComplete){
           Log.d("CCAADD", "getCurrentUserChats: result completed ")


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
                                val c = i.getValue(Chats::class.java)
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

                         //       createChatRoom(friendUID, post, context)

                            }
                        } else {
                            Log.d("UserHelper", "openChat:Don't have a chat..opening chat")

                        }

                    } else {
                        Log.d("UserHelper", "openChat: ${it.exception}")

                    }
                }


        }

         fun startChatWithFriend(friendUID: String?, chats: ArrayList<String>) {

             chats.add(friendUID!!)



            database.reference.child("User").child(auth.uid!!).child("chats")
                .setValue(chats)
                .addOnSuccessListener {
                    //now creating chat room
                    var data = mapOf("lastMsg" to "", "lastMsgTime" to "")

                    addChatInFriend(friendUID, auth.uid!!)


                }

        }

        private fun addChatInFriend(friendUID: String, uid: String) {
            database.reference.child("User").child(friendUID).child("chats").get().addOnCompleteListener {

                val chats= ArrayList<String>()


                if (it.isSuccessful){
                    Log.d("CC", "getCurrentUserChats: data fetched ")
                    if (it.result.exists()) {
                        Log.d("CCAADD", "getCurrentUserChats: result exist ")

                        for (i in it.result.children) {
                            val c = i.value.toString()
                            chats.add(c)
                        }

                    }
                   chats.add(uid)

                    database.reference.child("User").child(friendUID).child("chats")
                        .setValue(chats)
                        .addOnSuccessListener {

                            Log.d("CCAADD", "addChatInFriend: chat added in users")

                        }

                }
        }}

        private fun startChatActivity(context: HomeFragment, chat: Chats) {
            var intent = Intent(context.context, ChattingActivity::class.java)
            intent.putExtra("name", chat.friendName)
            intent.putExtra("picture", chat.friendProfileURI)
            intent.putExtra("uid", chat.friendUID)
            context.requireContext().startActivity(intent)
        }


        fun check(friendUID:String){

            checkIfChatExist(friendUID)




        }


        private fun checkIfChatExist(receiverUid: String) {

            getCurrentUserChats(object : ChatsCallBack{
                override fun onChatsFetched(chats: ArrayList<String>) {
                    var flag= false

                    chats.forEach{
                        if (it == receiverUid) flag = true
                    }

                    if(!flag){
                        startChatWithFriend(receiverUid,chats)
                    }
                }

            })

        }


    }
}