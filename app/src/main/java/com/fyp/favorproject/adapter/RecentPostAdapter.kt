package com.fyp.favorproject.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.model.RecentPostModel
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import java.util.*

class RecentPostAdapter(
    val context: Context,
    private val list: ArrayList<RecentPostModel>,
    private val mUserName: String,
    private val userImage: String,
    private val mUserDept: String,
   private val deleteFunction: (String, Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_FAVOR = 1
        const val VIEW_TYPE_BUY_SELL = 2
        const val VIEW_TYPE_LOST_FOUND = 3
    }


    private inner class FavorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tvUserNameFavRec)
        val profilePic: ImageView = itemView.findViewById(R.id.ivUserProfileFavRec)
        val postDate: TextView = itemView.findViewById(R.id.tvPostTimeFavRec)
        val userDept: TextView = itemView.findViewById(R.id.tvUserDepartmentFavRec)
        val postLikes: TextView = itemView.findViewById(R.id.btnLikeFavRec)
        val postDesc: TextView = itemView.findViewById(R.id.etPostDescriptionRecent)
        val postShare: TextView = itemView.findViewById(R.id.btnShareFavRec)
        val postDelete: TextView = itemView.findViewById(R.id.btnDeleteFavRec)
        val postImage: com.google.android.material.imageview.ShapeableImageView =
            itemView.findViewById(R.id.ivPostImageFavRec)

        fun bind(position: Int)
        {

            val currentFavor = list[position]
            Log.d("AAVV1122", "bind: favor ${currentFavor.viewType}")

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

            Picasso.get()
                .load(userImage)
                .placeholder(R.drawable.place_holder_image)
                .into(profilePic)
            userName.text = mUserName.toString()
            userDept.text = mUserDept.toString()


            val description = currentFavor.postDescription
            if (description == "") {
                postDesc.visibility = View.GONE
            } else {
                postDesc.text = currentFavor.postDescription
                postDesc.visibility = View.VISIBLE
            }

            //share functionality <------>


           postDelete.setOnClickListener{
               deleteFunction(currentFavor.postID,1)
           }
        }
    }


    private inner class BuySellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val profilePic: ImageView = itemView.findViewById(R.id.pro)
        //val userName: TextView = itemView.findViewById(R.id.tvUserName)
        //val postDate: TextView = itemView.findViewById(R.id.tvPostTime)
        //val userDept: TextView = itemView.findViewById(R.id.tvUserDepartment)
        //val postTime: TextView = itemView.findViewById(R.id.tvPostTime)
        val btnRespond: MaterialButton = itemView.findViewById(R.id.btnRespondBuyAndSaleRecent)
        val postDesc: TextView = itemView.findViewById(R.id.tvDescriptionBuySaleRecent)
        val postShare: TextView = itemView.findViewById(R.id.btnShareBuyAndSaleRecent)
        val postDelete: ImageButton = itemView.findViewById(R.id.btnDeleteRecent)
        val postImage: ImageView=
            itemView.findViewById(R.id.ivImageBuyAndSaleRecent)

        fun bind(position: Int) {

            val currentFavor = list[position]
            Log.d("AAVV1122", "bind: buy and sell called  ${currentFavor.viewType}")

            @Suppress("DEPRECATION")
            try {
                val date = "${Date(currentFavor.postTime!!).toLocaleString().subSequence(0, 11)} "

            }catch (e:Exception){
                Log.d("RecentPostA", "bind:  ${e.message}")
            }
           // postDate.text = date
        //    postLikes.text = "${currentFavor.postLikes}"
            if (currentFavor.postImage?.length!! > 5) {
                //PostImage
                Picasso.get()
                    .load(currentFavor.postImage)
                    .placeholder(R.drawable.cover_photo_place_holder)
                    .into(postImage)
            } else {
                postImage.visibility = View.GONE
            }

//            Picasso.get()
//                .load(userImage)
//                .placeholder(R.drawable.place_holder_image)
//                .into(profilePic)
//            userName.text = mUserName.toString()
//            userDept.text = mUserDept.toString()


            val description = currentFavor.postDescription
            if (description == "") {
                postDesc.visibility = View.GONE
            } else {
                postDesc.text = currentFavor.postDescription
                postDesc.visibility = View.VISIBLE
            }

            //share functionality <------>



        }
    }

    private inner class LostFoundViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            Log.d("AAVV1122", "bind: lost and found called ")

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
            VIEW_TYPE_FAVOR -> {(holder as FavorViewHolder).bind(position)

            holder.postDelete.setOnClickListener{
                deleteFunction(list[position].postID,1)
            }
            }
            VIEW_TYPE_BUY_SELL -> (holder as BuySellViewHolder).bind(position)
            VIEW_TYPE_LOST_FOUND -> (holder as LostFoundViewHolder).bind(position)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    override fun getItemCount() = list.size
}