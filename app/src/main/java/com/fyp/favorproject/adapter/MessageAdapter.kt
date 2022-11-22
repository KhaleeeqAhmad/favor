package com.fyp.favorproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.ReceivedMsgBinding
import com.fyp.favorproject.databinding.SendMsgBinding
import com.fyp.favorproject.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class MessageAdapter(
    var context: Context,
    messages: ArrayList<Message>?,
    senderRoom: String,
    receiverRoom: String
):RecyclerView.Adapter<RecyclerView.ViewHolder?>()
{
    lateinit var messages: ArrayList<Message>
    val ITEM_SENT = 1
    val ITEM_RECEIVED = 2
    val senderRoom: String
    val receiverRoom: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM_SENT){
            val view = LayoutInflater.from(context).inflate(R.layout.send_msg, parent, false)
            SendMsgHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.received_msg,parent,false)
            ReceiveMsgHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val messages = messages[position]
        return if (FirebaseAuth.getInstance().uid == messages.senderId){
            ITEM_SENT
        }
        else{
            ITEM_RECEIVED
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder.javaClass == SendMsgHolder::class.java){
            val viewHolder = holder as  SendMsgHolder
            if (message.message.equals("photo")){
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.VISIBLE
                viewHolder.binding.mLinear.visibility = View.VISIBLE
                Picasso.get().load(message.imageUrl).placeholder(R.drawable.placeholder).into(holder.binding.image)
            }
            viewHolder.binding.message.text = message.message
            viewHolder.itemView.setOnClickListener{

            }
        }
    }

    override fun getItemCount(): Int = messages.size

    inner class SendMsgHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding: SendMsgBinding = SendMsgBinding.bind(itemView)
    }
    inner class ReceiveMsgHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var binding: ReceivedMsgBinding = ReceivedMsgBinding.bind(itemView)
    }

    init {
        if (messages != null) {
            this.messages = messages
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }


}