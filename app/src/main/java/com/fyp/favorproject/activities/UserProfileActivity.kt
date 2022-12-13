package com.fyp.favorproject.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fyp.favorproject.R
import com.fyp.favorproject.adapter.RecentPostAdapter
import com.fyp.favorproject.databinding.ActivityUserProfileBinding
import com.fyp.favorproject.model.Post
import com.fyp.favorproject.model.RecentPostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
        private lateinit var list: ArrayList<RecentPostModel>
  lateinit var  recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        list = ArrayList()

        getUserData()


        binding.btnChangeCoverPhoto.setOnClickListener {
            contractForCoverPhoto.launch("image/*")
        }
        binding.btnChangeProfilePhoto.setOnClickListener {
            contractForProfilePhoto.launch("image/*")
        }

        recyclerView = binding.rvRecentPosts
        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setHasFixedSize(true)






    }

    private fun getAllPosts(name:String,image:String,dep:String) {

        val userPosts= ArrayList<RecentPostModel>()
        database.reference.child("favor").get().addOnCompleteListener{

                if (it.isSuccessful){
                    if (it.result.exists()){
                        it.result.children.forEach{p ->
                            val m = p.getValue(Post::class.java)!!
                            if (m.postedBy!! == auth.uid.toString()  ){
                                val k= RecentPostModel(1,m.postID,m.postImage,m.postedBy,m.postDescription!!,m.postTime,m.itemPrice,m.postLikes)
                                userPosts.add(k)
                            }
                        }
                    }
                }

            database.reference.child("buyAndSale").get().addOnCompleteListener{

                if (it.isSuccessful){
                    if (it.result.exists()){
                        it.result.children.forEach{p ->
                            val m = p.getValue(Post::class.java)!!
                            if (m.postedBy!! == auth.uid.toString()  ){
                                val k= RecentPostModel(2,m.postID,m.postImage,m.postedBy,m.postDescription!!,m.postTime,m.itemPrice,m.postLikes)
                                userPosts.add(k)
                            }
                        }
                    }
                }

                database.reference.child("lostAndFound").get().addOnCompleteListener{
                    if (it.isSuccessful){
                        if (it.result.exists()){
                            it.result.children.forEach{p ->
                                val m = p.getValue(Post::class.java)!!
                                if (m.postedBy!! == auth.uid.toString()  ){
                                    val k= RecentPostModel(3,m.postID,m.postImage,m.postedBy,m.postDescription!!,m.postTime,m.itemPrice,m.postLikes)
                                    userPosts.add(k)
                                }
                            }
                        }

                     userPosts.sortedBy { it.postTime }

                        Toast.makeText(this, "${userPosts.size}", Toast.LENGTH_SHORT).show()
                        val userAdapter = RecentPostAdapter(this@UserProfileActivity, userPosts,name,image,dep,deleteFunction)
                        recyclerView.adapter = userAdapter
                        recyclerView.visibility = View.VISIBLE


                    }else{
                     userPosts.sortedBy { it.postTime }
                        Toast.makeText(this, "${userPosts.size}", Toast.LENGTH_SHORT).show()

                        val userAdapter = RecentPostAdapter(this@UserProfileActivity, userPosts,name,image,dep,deleteFunction)
                        recyclerView.adapter = userAdapter
                        recyclerView.visibility = View.VISIBLE

                    }
                }
            }

        }

    }


    private fun getUserData() {
        val userProfile = database.reference.child("User")
            .child(FirebaseAuth.getInstance().uid!!)
        userProfile.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(com.fyp.favorproject.model.User::class.java)
                    Picasso.get()
                        .load(user!!.userProfilePhoto)
                        .placeholder(R.drawable.place_holder_image)
                        .into(binding.ivUserProfilePhoto)
                    Picasso.get()
                        .load(user.userCoverPhoto)
                        .placeholder(R.drawable.cover_photo_place_holder)
                        .into(binding.ivUserCoverPhoto)

                    binding.tvProfileUserName.text = user.name
                    binding.tvUserDepartment.text = user.department

                    getAllPosts(user.name.toString(),user!!.userProfilePhoto.toString(),user.department.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    private val contractForCoverPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.ivUserCoverPhoto.setImageURI(it)
        val referenceCoverPhoto = storage.reference.child("cover_photo").child(auth.uid!!)

        referenceCoverPhoto.putFile(it!!).addOnSuccessListener {
            referenceCoverPhoto.downloadUrl.addOnSuccessListener { imageLink ->
                Toast.makeText(this@UserProfileActivity, "Cover photo saved!", Toast.LENGTH_SHORT).show()

                database.reference.child("User").child(auth.uid!!).child("userCoverPhoto").setValue(imageLink.toString())
            }
        }
    }

    private val contractForProfilePhoto = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.ivUserProfilePhoto.setImageURI(it)
        val referenceCoverPhoto = storage.reference.child("profile_photo").child(auth.uid!!)

        referenceCoverPhoto.putFile(it!!).addOnSuccessListener {
            referenceCoverPhoto.downloadUrl.addOnSuccessListener { imageLink ->
                Toast.makeText(this@UserProfileActivity, "Profile photo saved!", Toast.LENGTH_SHORT).show()
                database.reference.child("User").child(auth.uid!!).child("userProfilePhoto").setValue(imageLink.toString())
            }
        }
    }


    val deleteFunction= fun(postID:String, postGroup:Int){

        var groupName=""
        when(postGroup){
            1 ->{ groupName="favor"}//favor
            2 ->{ groupName="buyAndSale"}//buy and sell
            3 -> {groupName="lostAndFound"}//lost and foun
        }

        database.reference.child(groupName).child(postID).setValue(null).addOnSuccessListener {
            Toast.makeText(this, "Post Deleted", Toast.LENGTH_SHORT).show()
            getUserData()
        }
    }
}


