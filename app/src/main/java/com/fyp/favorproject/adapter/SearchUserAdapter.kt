package com.fyp.favorproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.model.User
import com.squareup.picasso.Picasso

class SearchUserAdapter(private val list: ArrayList<User>, val messageListener: (String) -> Unit)
    : RecyclerView.Adapter<SearchUserAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.ivUserProfileSearch)
        val userName: TextView = itemView.findViewById(R.id.tvUserNameSearch)
        val userLevel: TextView = itemView.findViewById(R.id.tvLevelSearch)
        val message:androidx.appcompat.widget.AppCompatImageButton = itemView.findViewById(R.id.btnMessage_search)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_search,parent,false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        Picasso.get()
            .load(item.userProfilePhoto)
            .into(holder.profileImage)

        holder.userName.text = item.name
        holder.userLevel.text = "Level 3"

        holder.message.setOnClickListener {
            messageListener(item.userID!!)
        }
    }

    override fun getItemCount() = list.size

}