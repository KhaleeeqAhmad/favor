package com.fyp.favorproject.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.fragments.BuySaleFragment
import com.fyp.favorproject.model.Post
import com.squareup.picasso.Picasso


class BuySaleAdapter(
    val context : BuySaleFragment,
    private val buySaleList: ArrayList<Post>
    ) : RecyclerView.Adapter<BuySaleAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_dashboard_buy_sale, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPost = buySaleList[position]

        //PostImage
        Picasso.get()
            .load(currentPost.postImage)
            .placeholder(R.drawable.cover_photo_place_holder)
            .into(holder.postImage)

        holder.postDescription.text = currentPost.postDescription
        holder.itemPrice.text = "Rs: ${currentPost.itemPrice}"

    }

    override fun getItemCount() = buySaleList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val postDescription : TextView = itemView.findViewById(R.id.tvDescriptionBuySale)
        val postImage: ImageView = itemView.findViewById(R.id.ivImageBuyAndSale)
        val itemPrice: TextView = itemView.findViewById(R.id.tvPrice)
    }

}