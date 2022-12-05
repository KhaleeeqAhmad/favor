package com.fyp.favorproject.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.fragments.HomeFragment
import com.fyp.favorproject.mainFragment.ChattingActivity
import com.fyp.favorproject.model.Post
import com.fyp.favorproject.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class FavorAdapter(
    val context: HomeFragment,
    private val postList: ArrayList<Post>
) : RecyclerView.Adapter<FavorAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_dashboard_favor, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentFavor = postList[position]

        //PostImage
        Picasso.get()
            .load(currentFavor.postImage)
            .placeholder(R.drawable.cover_photo_place_holder)
            .into(holder.postImage)

        @Suppress("DEPRECATION") val date = "${java.util.Date(currentFavor.postTime!!).toLocaleString().subSequence(0,11)} "

        holder.postDate.text = date

        //User Data
        FirebaseDatabase.getInstance().reference.child("User").child(currentFavor.postedBy!!)
        .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)

                Picasso.get()
                   .load(user?.userProfilePhoto)
                    .placeholder(R.drawable.place_holder_image)
                    .into(holder.userProfile)
                holder.userName.text = user?.name
                holder.userDepartment.text = user?.department
            }

            override fun onCancelled(error: DatabaseError) = Unit

        })

        val description = currentFavor.postDescription
        if (description.equals("")) {
            holder.postDescription.visibility = View.GONE
        } else {
            holder.postDescription.text = currentFavor.postDescription
            holder.postDescription.visibility = View.VISIBLE
        }


        holder.postResponse.setOnClickListener {

            val friendUID= currentFavor.postedBy.toString()

            val intent = Intent(context.requireContext(), ChattingActivity::class.java).apply {
                putExtra("friendUID", friendUID)
            }

            context.startActivity(intent)
        }




//        holder.postLikes.setOnClickListener {
//            FirebaseDatabase.getInstance().reference
//                .child("favor").child(currentFavor.postID!!)
//                .child("likes")
//                .child(FirebaseAuth.getInstance().uid!!)
//                .setValue(true).addOnSuccessListener {
//                    FirebaseDatabase.getInstance().reference
//                        .child("favor").child(currentFavor.postID!!)
//                        .child("postLikes")
//                        .setValue(currentFavor.postLikes?.plus(1)).addOnSuccessListener {
//                            holder.postLikes.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_post_liked,0)
//                            holder.postLikes.setTextColor(R.color.my_blue_primary)
//                            holder.totalLikes.text = currentFavor.postLikes.toString()
//                        }
//                }
//        }

    }

    override fun getItemCount() = postList.size


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userProfile: ImageView = itemView.findViewById(R.id.ivUserProfile)
        val userName : TextView = itemView.findViewById(R.id.tvUserName)
        val userDepartment: TextView = itemView.findViewById(R.id.tvUserDepartment)
        val postDate: TextView = itemView.findViewById(R.id.tvPostTime)
        val postDescription : TextView = itemView.findViewById(R.id.etPostDescription)
        val postImage: ImageView = itemView.findViewById(R.id.ivPostImage)
        val postLikes: TextView = itemView.findViewById(R.id.btnLike)
        val postResponse: TextView = itemView.findViewById(R.id.btnRespond)
        val postShare: TextView = itemView.findViewById(R.id.btnShare)
        val totalLikes: TextView = itemView.findViewById(R.id.tvLikes)




    }
}