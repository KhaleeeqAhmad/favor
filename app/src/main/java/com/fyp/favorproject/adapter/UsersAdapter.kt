package com.fyp.favorproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.ChatRowItemBinding
import com.fyp.favorproject.model.Users
import com.squareup.picasso.Picasso

class UsersAdapter(val usersArrayList: ArrayList<Users>) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var binding = ChatRowItemBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.chat_row_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = usersArrayList[position]
        Picasso.get().load(item.profilePic).placeholder(R.drawable.avataar).into(holder.binding.ivProfilePicChatsitem)
        holder.binding.tvUsernameChatsitem.text = item.name
        holder.binding.tvLastmessaageChatsitem.text = item.lastMessage
    }

    override fun getItemCount(): Int = usersArrayList.size
}

