package com.fyp.favorproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.model.RecentPostModel

class RecentPostAdapter(
    val context: Context,
    private val list: List<RecentPostModel>
): RecyclerView.Adapter<RecentPostAdapter.RecentPostViewHolder>() {

    inner class RecentPostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val posImage: ImageView = itemView.findViewById(R.id.postImageRecent)
        val postDescription: TextView = itemView.findViewById(R.id.postDescriptionRecent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentPostViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard_recent_posts,parent,false)
        return RecentPostViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: RecentPostViewHolder, position: Int) {
        val item = list[position]
        holder.posImage.setImageResource(item.postImage)
        holder.postDescription.text = item.postDescription
    }

    override fun getItemCount() = list.size


}