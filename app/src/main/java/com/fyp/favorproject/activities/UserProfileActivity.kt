package com.fyp.favorproject.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.ActivityUserProfileBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        getUserData()

        binding.btnChangeCoverPhoto.setOnClickListener {
            contractForCoverPhoto.launch("image/*")
        }
        binding.btnChangeProfilePhoto.setOnClickListener {
            contractForProfilePhoto.launch("image/*")
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
}


