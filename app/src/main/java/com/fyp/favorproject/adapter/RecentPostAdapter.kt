package com.fyp.favorproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.model.RecentPostModel
import com.fyp.favorproject.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.*

class RecentPostAdapter(
    val context: Context,
    private val list: ArrayList<RecentPostModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_FAVOR = 1
        const val VIEW_TYPE_BUY_SELL = 2
        const val VIEW_TYPE_LOST_FOUND = 3
    }


    private inner class FavorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val profilePic: ImageView = itemView.findViewById(R.id.ivUserProfile)
        val postDate: TextView = itemView.findViewById(R.id.tvPostTime)
        val userDept: TextView = itemView.findViewById(R.id.tvUserDepartment)
        val postLikes: TextView = itemView.findViewById(R.id.btnLike)
        val postDesc: TextView = itemView.findViewById(R.id.etPostDescription)
        val postShare: TextView = itemView.findViewById(R.id.btnShare)
        val postDelete: TextView = itemView.findViewById(R.id.btnDelete)
        val postImage: com.google.android.material.imageview.ShapeableImageView =
            itemView.findViewById(R.id.ivPostImage)

        fun bind(position: Int) {
            val currentFavor = list[position]

            @Suppress("DEPRECATION")
            val date = "${Date(currentFavor.postTime!!).toLocaleString().subSequence(0, 11)} "
            postDate.text = date
            postLikes.text = "${currentFavor.postLikes}"
            if (currentFavor.postImage?.length!! > 5) {
                //PostImage
                Picasso.get()
                    .load(currentFavor.postImage)
                    .placeholder(R.drawable.cover_photo_place_holder)
                    .into(postImage)
            } else {
                postImage.visibility = View.GONE
            }
            FirebaseDatabase.getInstance().reference.child("User").child(currentFavor.postedBy!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        Picasso.get()
                            .load(user?.userProfilePhoto)
                            .placeholder(R.drawable.place_holder_image)
                            .into(profilePic)
                        userName.text = user?.name
                        userDept.text = user?.department
                    }

                    override fun onCancelled(error: DatabaseError) = Unit
                })


            val description = currentFavor.postDescription
            if (description == "") {
                postDesc.visibility = View.GONE
            } else {
                postDesc.text = currentFavor.postDescription
                postDesc.visibility = View.VISIBLE
            }

            //share functionality <------>


            //post delete
            val btnDel: TextView = itemView.findViewById(R.id.btnDelete)
            btnDel.setOnClickListener {

            }
        }
    }


    private inner class BuySellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePic: ImageView = itemView.findViewById(R.id.ivUserProfile)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val postDate: TextView = itemView.findViewById(R.id.tvPostTime)
        val userDept: TextView = itemView.findViewById(R.id.tvUserDepartment)
        val postTime: TextView = itemView.findViewById(R.id.tvPostTime)
        val postDesc: TextView = itemView.findViewById(R.id.etPostDescription)
        val postShare: TextView = itemView.findViewById(R.id.btnShare)
        val postDelete: TextView = itemView.findViewById(R.id.btnDelete)
        val postImage: com.google.android.material.imageview.ShapeableImageView =
            itemView.findViewById(R.id.ivPostImage)


        fun bind(position: Int) {

        }
    }

    private inner class LostFoundViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_FAVOR) {
            return FavorViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_profile_favor, parent, false)
            )
        } else if (viewType == VIEW_TYPE_BUY_SELL) {
            return BuySellViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.item_profile_buy_sell, parent, false)
            )
        }
        return LostFoundViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_profile_lost_found, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (list[position].viewType) {
            VIEW_TYPE_FAVOR -> (holder as FavorViewHolder).bind(position)
            VIEW_TYPE_BUY_SELL -> (holder as BuySellViewHolder).bind(position)
            VIEW_TYPE_LOST_FOUND -> (holder as LostFoundViewHolder).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    override fun getItemCount() = list.size
}