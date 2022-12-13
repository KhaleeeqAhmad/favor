package com.fyp.favorproject.mainFragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.fyp.favorproject.R
import com.fyp.favorproject.adapter.MessageAdapter
import com.fyp.favorproject.databinding.ActivityChattingBinding
import com.fyp.favorproject.model.Chats
import com.fyp.favorproject.model.Message
import com.fyp.favorproject.model.User
import com.fyp.favorproject.utill.OnChatStart
import com.fyp.favorproject.utill.UserHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ChattingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChattingBinding
    lateinit var adapter: MessageAdapter
    lateinit var messages: ArrayList<Message>
    lateinit var senderRoom: String
    lateinit var receiverRoom: String
     val database: FirebaseDatabase = FirebaseDatabase.getInstance()

     val storage: FirebaseStorage =FirebaseStorage.getInstance()
    lateinit var senderUid: String
    lateinit var receiverUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val friendUID =intent.getStringExtra("friendUID")

        receiverUid=""
        if (friendUID == null){

            Toast.makeText(this, "Sorry! Can't start chat. Please try again later", Toast.LENGTH_SHORT).show()
         return
        }else{
            binding.progressBarChattingActivity.visibility= View.VISIBLE
            receiverUid= friendUID.toString()
            UserHelper().check(receiverUid, object : OnChatStart{
                override fun onChatStart(friend: User, currentUser: User, chats: ArrayList<Chats>) {

                    binding.progressBarChattingActivity.visibility= View.GONE
                    //Toast.makeText(this@ChattingActivity, "${friend.userID}", Toast.LENGTH_SHORT).show()

                    setSupportActionBar(binding.chattingToolbar)

                    messages = ArrayList()
                    val name = intent.getStringExtra("name").toString()
                    val profile = intent.getStringExtra("picture").toString()
                    binding.tvMessageUserName.text = friend.name

                   try {
                       Glide.with(this@ChattingActivity).load(friend.userProfilePhoto).placeholder(R.drawable.placeholder).into(binding.ivMessageProfilePic)

                   }catch (e:Exception){
                       Log.d("QQ", "onChatStart: ${e.message} ")
                   }
                    binding.ivBackButton.setOnClickListener{finish()}
//        receiverUid = intent.getStringExtra(    "uid").toString()
                       senderUid = FirebaseAuth.getInstance().uid.toString()
                        checkUserPresence()
                    senderRoom = senderUid + receiverUid
                    receiverRoom = receiverUid + senderUid
                    adapter = MessageAdapter(this@ChattingActivity, messages, senderRoom, receiverRoom)
                    binding.rvMessagesRecycler.layoutManager = LinearLayoutManager(this@ChattingActivity)
                    binding.rvMessagesRecycler.adapter = adapter
                    database.reference.child("Chats").child(senderRoom).child("message")
                        .addValueEventListener(object : ValueEventListener  {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                //Toast.makeText(this@ChattingActivity, "New Message", Toast.LENGTH_SHORT).show()
                                messages.clear()
                                for (snapshot1 in snapshot.children){
                                    val message: Message? = snapshot1.getValue(Message::class.java)
                                    message!!.messageId = snapshot1.key
                                    messages.add(message)
                                }
                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {}

                        })

                    binding.ivSendBtn.setOnClickListener {
                        val messageTxt: String = binding.etMessageBox.text.toString()
                        val date = Date()
                        val message = Message(messageTxt, senderUid, date.time)
                        binding.etMessageBox.setText("")
                        val randomKey = database.reference.push().key
                        val lastMsgObj = HashMap<String, Any>()
                        lastMsgObj["lastMsg"] = message.message!!
                        lastMsgObj["lastMsgTime"] = date.time
                        database.reference.child("Chats").child(senderRoom).updateChildren(lastMsgObj)
                        database.reference.child("Chats").child(receiverRoom).updateChildren(lastMsgObj)
                        database.reference.child("Chats").child(senderRoom).child("message").child(randomKey!!)
                            .setValue(message).addOnSuccessListener {
                                database.reference.child("Chats").child(receiverRoom).child("message").child(
                                    randomKey
                                )
                                    .setValue(message).addOnSuccessListener {  }
                            }
                    }

                    binding.ivMessageAttachment.setOnClickListener{
                        val intent = Intent()
                        intent.action = Intent.ACTION_GET_CONTENT
                        intent.type = "image/*"
                        startActivityForResult(intent, 25)
                    }

                    val handler = Handler()
                    binding.etMessageBox.addTextChangedListener(object: TextWatcher{
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun afterTextChanged(p0: Editable?) {
                            database.reference.child("Presence").child(senderUid).setValue("Typing")
                            handler.removeCallbacksAndMessages(null)
                            handler.postDelayed(userStoppedTyping, 1000)
                            if (binding.etMessageBox.text.toString().isEmpty()){
                                binding.ivSendBtn.isEnabled=false

                                binding.ivSendBtn.setImageDrawable(getDrawable(R.drawable.ic_send_disabled))
                            }else{
                                binding.ivSendBtn.isEnabled=true
                                binding.ivSendBtn.setImageDrawable(getDrawable(R.drawable.ic_send))
                            }
                        }
                        val userStoppedTyping = Runnable {
                            database.reference.child("Presence").child(senderUid).setValue("Online")
                        }
                    })

                    supportActionBar?.setDisplayShowTitleEnabled(false)
                }

            })
        }



    }

    private fun checkUserPresence() {
        database.reference.child("Presence").child(receiverUid).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val status = snapshot.value.toString()
                    if (status == "offline"){
                        binding.tvMessageUserStatus.visibility = View.GONE
                    }
                    else{
                        binding.tvMessageUserStatus.setText(status)
                        binding.tvMessageUserStatus.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 25 && data != null && data.data != null){
            val selectedImage = data.data
            val calender = Calendar.getInstance()
            val reference = storage.reference.child("Chats").child(calender.timeInMillis.toString()+"")
            reference.putFile(selectedImage!!).addOnCompleteListener{
                if (it.isSuccessful){
                    reference.downloadUrl.addOnSuccessListener { uri ->
                        val filePath = uri.toString()
                        val messageTxt: String = binding.etMessageBox.text.toString()
                        val date = Date()
                        val message = Message(messageTxt, senderUid, date.time)
                        message.message = "photo"
                        message.imageUrl = filePath
                        binding.etMessageBox.setText("")
                        val randomKey = database.reference.push().key
                        val lastMsgObj = HashMap<String, Any>()
                        lastMsgObj["lastMsg"] = message.message!!
                        lastMsgObj["lastMsgTime"] = date.time
                        database.reference.child("Chats").updateChildren(lastMsgObj)
                        database.reference.child("Chats").child("receiverRoom").updateChildren(lastMsgObj)
                        database.reference.child("Chats").child("senderRoom").child("messages").child(randomKey!!)
                            .setValue(message).addOnSuccessListener { database.reference.child("Chats").child(receiverRoom)
                                .child("messages").child(randomKey).setValue(message).addOnSuccessListener {  }}

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database.reference.child("Presence").child(currentId!!).setValue("Online")
    }
    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database.reference.child("Presence").child(currentId!!).setValue("Offline")
    }


}