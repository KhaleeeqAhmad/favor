package com.fyp.favorproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.fragments.LostFoundFragment
import com.fyp.favorproject.model.Post
import com.fyp.favorproject.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class LostAndFoundAdapter(
    val context: LostFoundFragment,
    private val lostFoundList: ArrayList<Post>
) : RecyclerView.Adapter<LostAndFoundAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_dachboard_lost_found, parent, false)
        return MyViewHolder(itemView)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPost = lostFoundList[position]

        Picasso.get()
            .load(currentPost.postImage)
            .placeholder(R.drawable.cover_photo_place_holder)
            .into(holder.postImage)

        @Suppress("DEPRECATION") val date = "${java.util.Date(currentPost.postTime!!).toLocaleString().subSequence(0,11)} "

        holder.postDate.text = date
        val description = currentPost.postDescription
        if (description.equals("")) {
            holder.postDescription.visibility = View.GONE
        } else {
            holder.postDescription.visibility = View.VISIBLE
            holder.postDescription.text = currentPost.postDescription
        }

//        holder.postTotalResponses.setOnClickListener{
//            if (currentPost.postedBy.toString() == FirebaseAuth.getInstance().uid!!) {
//                Toast.makeText(context.requireContext(), "This is your post; Can't open chat", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
//            clickResponse.clickResponse(currentPost.postedBy.toString())
//        }


//        holder.postTotalResponses.setOnClickListener{
//            if (currentPost.postedBy.toString() == FirebaseAuth.getInstance().uid!!){
//                Toast.makeText(context.requireContext(), "You can't response to your post", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val friendUID = currentPost.postedBy.toString()
//
//            val intent = Intent(context?.requireContext(), ChattingActivity::class.java).apply {
//
//                putExtra("friendUID", friendUID)
//            }
//            context.startActivity(intent)
//        }

        //Lost Found Data
        FirebaseDatabase.getInstance().reference.child("User").child(currentPost.postedBy!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)

                    Picasso.get()
                        .load(user?.userProfilePhoto)
                        .placeholder(R.drawable.place_holder_image)
                        .into(holder.userProfile)
                    holder.userName.text = user?.name
                }
                override fun onCancelled(error: DatabaseError) = Unit
            })
    }

    override fun getItemCount() = lostFoundList.size



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userProfile: ImageView = itemView.findViewById(R.id.ivUserProfile)
        val userName : TextView = itemView.findViewById(R.id.tvUserName)
        val postDate: TextView = itemView.findViewById(R.id.tvPostTime)
        val postDescription : TextView = itemView.findViewById(R.id.etPostDescription)
        val postImage: ImageView = itemView.findViewById(R.id.ivPostImage)
  //      val postTotalResponses: ImageButton = itemView.findViewById(R.id.btnRespondLnF)
  //      val postShares: ImageButton = itemView.findViewById(R.id.btnShare)

    }
}