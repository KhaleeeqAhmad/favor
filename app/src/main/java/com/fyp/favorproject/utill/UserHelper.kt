package com.fyp.favorproject.utill

import android.util.Log
import com.fyp.favorproject.model.Chats
import com.fyp.favorproject.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

private val auth = FirebaseAuth.getInstance()
private val database = FirebaseDatabase.getInstance()

private const val TAG= "AABBCC"

class UserHelper {

    private var mFriend: User?= null
    private var mCurrentUser: User?= null
    private var mFriendChats= ArrayList<Chats>()
    private var mCurrentChats= ArrayList<Chats>()
    private lateinit var mainCallBack:OnChatStart
    private var OMG1= true
    private var OMG2= true



    companion object {
        fun getCurrentUserChats(callBack: ChatsCallBack) {


            val currentUser = auth.currentUser
            val chats: ArrayList<Chats> = ArrayList()

            Log.d("CCAADD", "getCurrentUserChats:called ")
            database.reference.child("User").child(currentUser!!.uid).child("chats").get().addOnCompleteListener {

                 if (it.isSuccessful){
                     Log.d("CC", "getCurrentUserChats: data fetched ")
                     if (it.result.exists()) {
                         Log.d("CCAADD", "getCurrentUserChats: result exist ")

                         for (i in it.result.children) {
                             val c = i.getValue(Chats::class.java)!!
                             chats.add(c)
                         }

                     }
                    callBack.onChatsFetched(chats)

                 }
       if (it.isCanceled){
           Log.d("CCAADD", "getCurrentUserChats: result cancelled ")
       //    callBack.onChatsFetched(chats)
       }


       if (it.isComplete){
           Log.d("CCAADD", "getCurrentUserChats: result completed ")


     //     callBack.onChatsFetched(chats)
       }
             }





        }

    }

    fun check(friendUID:String , callBack:OnChatStart){
        mainCallBack=callBack
        checkIfChatExist(friendUID)




    }

    private fun checkIfChatExist(receiverUid: String) {

        getCurrentUserChats(object : ChatsCallBack{
            override fun onChatsFetched(chats: ArrayList<Chats>) {
                var flag= false

                chats.forEach{
                    if (it.friendUID == receiverUid) flag = true
                }

                if(!flag){

                    getFriend(receiverUid)


                }else{

                    getFriend(receiverUid,true)

                }
            }

            override fun onFriendsMetaDataFetched(friend: User) = Unit

        })

    }

    private fun getFriend(friendUID: String){
        //User Data
        FirebaseDatabase.getInstance().reference.child("User").child(friendUID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)

                    mFriend=user
                    mFriend!!.userID= friendUID

                    getCurrentUser(auth.uid!!)

                }

                override fun onCancelled(error: DatabaseError) = Unit

            })
    }
    private fun getCurrentUser(currentUID: String){

        if (OMG1){
            //User Data
            FirebaseDatabase.getInstance().reference.child("User").child(currentUID)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)

                        OMG1= false
                        mCurrentUser=user
                        mCurrentUser!!.userID= currentUID
                        getCurrentUserChat(auth.uid!!)

                    }

                    override fun onCancelled(error: DatabaseError) = Unit

                })
        }

    }

    private fun getFriendChats(friendUID: String){

        val chats: ArrayList<Chats> = ArrayList()

        database.reference.child("User").child(friendUID).child("chats").get().addOnCompleteListener {

            if (it.isSuccessful){
                Log.d("CC", "getCurrentUserChats: data fetched ")
                if (it.result.exists()) {
                    Log.d("CCAADD", "getCurrentUserChats: result exist ")

                    for (i in it.result.children) {
                        val c = i.getValue(Chats::class.java)!!
                        chats.add(c)
                    }

                }
               mFriendChats= chats


                saveChatInUsers()


            }
            if (it.isCanceled){
                Log.d("CCAADD", "getCurrentUserChats: result cancelled ")

            }


            if (it.isComplete){
                Log.d("CCAADD", "getCurrentUserChats: result completed ")

            }
        }



    }

    private fun saveChatInUsers() {

        val chat1= Chats(mFriend!!.userID!!,mFriend!!.name,
        mFriend!!.userProfilePhoto,"","",Date().time.toString(),"")
        addInCurrentUser(chat1)


    }

    private fun addInCurrentUser(chat1: Chats) {

        mCurrentChats.add(chat1)

        database.reference.child("User").child(mCurrentUser!!.userID!!).child("chats")
            .setValue(mCurrentChats)
            .addOnSuccessListener {

                val chat2 =Chats(mCurrentUser!!.userID,mCurrentUser!!.name,
                    mCurrentUser!!.userProfilePhoto,"","",Date().time.toString(),"")

                addInFriend(chat2)
            }
    }

    private fun addInFriend(chat2: Chats) {

        mFriendChats.add(chat2)

        database.reference.child("User").child(mFriend!!.userID!!).child("chats")
            .setValue(mFriendChats)
            .addOnSuccessListener {

                mainCallBack.onChatStart(mFriend!!,mCurrentUser!!, ArrayList())
            }
    }

    private fun getCurrentUserChat(currentUID: String){

        if (OMG2){
        val chats: ArrayList<Chats> = ArrayList()

        database.reference.child("User").child(currentUID).child("chats").get().addOnCompleteListener {

            if (it.isSuccessful){
                Log.d("CC", "getCurrentUserChats: data fetched ")
                if (it.result.exists()) {
                    Log.d("CCAADD", "getCurrentUserChats: result exist ")

                    for (i in it.result.children) {
                        val c = i.getValue(Chats::class.java)!!
                        chats.add(c)
                    }

                }
                OMG2=false
                mCurrentChats= chats
                getFriendChats(mFriend!!.userID!!
                )

            }
            if (it.isCanceled){
                Log.d("CCAADD", "getCurrentUserChats: result cancelled ")

            }


            if (it.isComplete){
                Log.d("CCAADD", "getCurrentUserChats: result completed ")

            }
        }

        }

    }



    // if chat exists
    private fun getCurrentUser(currentUID: String, isChatExist:Boolean){

        if (OMG1){
            //User Data
            FirebaseDatabase.getInstance().reference.child("User").child(currentUID)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)

                        OMG1= false
                        mCurrentUser=user
                        mCurrentUser!!.userID= currentUID
                        mainCallBack.onChatStart(mFriend!!,mCurrentUser!!, ArrayList())


                    }

                    override fun onCancelled(error: DatabaseError) = Unit

                })
        }

    }

    private fun getFriend(friendUID: String, isChatExist: Boolean){
        //User Data
        FirebaseDatabase.getInstance().reference.child("User").child(friendUID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)

                    mFriend=user
                    mFriend!!.userID= friendUID

                        getCurrentUser(auth!!.uid!!,true)
                }

                override fun onCancelled(error: DatabaseError) = Unit

            })
    }




}