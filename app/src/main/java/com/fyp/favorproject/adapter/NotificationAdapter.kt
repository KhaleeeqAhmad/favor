package com.fyp.favorproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.fragments.NotificationFragment
import com.fyp.favorproject.model.NotificationModel

class NotificationAdapter(
    val context: NotificationFragment,
    private val list: List<NotificationModel>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_notification,parent,false)
        return NotificationViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = list[position]
        holder.profile.setImageResource(item.profile)
        holder.notificationDescription.text = item.notificationDescription
        holder.notificationTime.text = item.notificationTime
    }

    override fun getItemCount() = list.size


    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profile: ImageView = itemView.findViewById(R.id.ivUserProfileNotification)
        val notificationDescription: TextView = itemView.findViewById(R.id.tvNotificationDescription)
        val notificationTime: TextView = itemView.findViewById(R.id.tvNotificationTime)
    }

}