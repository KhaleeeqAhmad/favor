package com.fyp.favorproject.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.ActivityAskFavorBinding
import com.fyp.favorproject.model.Notification
import com.fyp.favorproject.model.Post
import com.fyp.favorproject.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

@Suppress("DEPRECATION")
class AskFavorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAskFavorBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var dialog: ProgressDialog

    //  Dialog post

    private var selectedOptionIndex  = 0
    private val options = arrayOf("Favor", "Buy & Sale", "Lost & found")
    private var selectedOption = options[selectedOptionIndex]


    private val image = 1
    private var imageUri: Uri = "".toUri()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskFavorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        dialog = ProgressDialog(this@AskFavorActivity)

        activityUiSetUp()
        userData()

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog.setTitle("Post Uploading")
        dialog.setMessage("Please wait...")
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }


    private fun activityUiSetUp() {

        binding.etPostDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val description = binding.etPostDescription.text.toString()
                val image = binding.showImage.isInLayout

                if (description.isNotEmpty() || image){
                    binding.btnPost.isEnabled = true
                    binding.btnPost.background= AppCompatResources.getDrawable(this@AskFavorActivity, R.drawable.post_button)
                    binding.btnPost.setTextColor(resources.getColor(R.color.white))
                }else{
                    binding.btnPost.isEnabled = false
                    binding.btnPost.background= AppCompatResources.getDrawable(this@AskFavorActivity,R.drawable.border)
                    binding.btnPost.setTextColor(resources.getColor(R.color.mediumGray))
                }
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        binding.btnClose.setOnClickListener {
            this@AskFavorActivity.finish()
        }

        binding.btnSelectImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Select IMAGE"),image)
        }

        binding.btnPost.setOnClickListener {
            uploadPost()
        }

        binding.btnSelectPostType.text = options[selectedOptionIndex]
        binding.btnSelectPostType.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Choose Option")
                .setSingleChoiceItems(options, selectedOptionIndex) { _, which ->
                    selectedOptionIndex = which
                    selectedOption = options[which]
                }
                .setPositiveButton("OK") { _, _ ->

                    snackBar(it, "$selectedOption Select")
                    binding.btnSelectPostType.text = selectedOption
                    if (selectedOption == options[1]) {
                        binding.itemPrice.visibility = View.VISIBLE
                    } else
                        binding.itemPrice.visibility = View.GONE

                }
                .setNeutralButton("Cancel") { _, _ ->
                }
                .show()
        }

    }


    private fun snackBar(view: View, msg: String) {
        Snackbar.make(view,msg, Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun userData() {

        val userRef = database.reference.child("User")
            .child(FirebaseAuth.getInstance().uid!!)

        userRef.addValueEventListener(object : ValueEventListener  {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    Picasso.get()
                        .load(user!!.userProfilePhoto)
                        .into(binding.ivUserProfile)
                    binding.tvUserName.text = user.name

                }
            }
            override fun onCancelled(error: DatabaseError) = Unit

        })
    }

    private fun uploadPost() {


        if (imageUri.toString().length>5){
            // post has  image
            dialog.show()
            if (binding.btnSelectPostType.text.equals(options[0])) {
                dialog.show()
                val postImageRef = storage.reference
                    .child("favor")
                    .child(auth.uid!!)
                    .child(Date().time.toString())
                postImageRef.putFile(imageUri).addOnSuccessListener {
                    postImageRef.downloadUrl
                        .addOnSuccessListener {
                            val postUploadData0 = Post()
                            postUploadData0.postImage = it.toString()
                            postUploadData0.postedBy = auth.uid
                            postUploadData0.postDescription = binding.etPostDescription.text.toString()
                            postUploadData0.postTime = Date().time

            val postUploadData = Post()
            postUploadData.postImage = ""
            postUploadData.postedBy = auth.uid
            postUploadData.postDescription = binding.etPostDescription.text.toString()
            postUploadData.postTime = Date().time

             val postID= database.reference
                 .child("favor").push().key
            postUploadData.postID=postID.toString()
            database.reference
                .child("favor")
                .child(postID!!)
                .setValue(postUploadData)
                .addOnSuccessListener {
                    dialog.dismiss()
                    Toast.makeText(
                        this@AskFavorActivity,
                        "Post posted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                            database.reference
                                .child("favor")
                                .push()
                                .setValue(postUploadData0)
                                .addOnSuccessListener {
                                    dialog.dismiss()
                                    Toast.makeText(
                                        this@AskFavorActivity,
                                        "Post posted Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val notification = Notification()
                                    notification.notificationBy = FirebaseAuth.getInstance().uid
                                    notification.notificationTime = Date().time
                                    notification.postID = postUploadData0.postID
                                    notification.postedBy = postUploadData0.postedBy
                                    notification.notificationType = " favor"
                                }
                        }
                }
            }
            if (binding.btnSelectPostType.text.equals(options[1])) {
                dialog.show()
                val postImageRef = storage.reference
                    .child("buy_and_sale")
                    .child(auth.uid!!)
                    .child(Date().time.toString())
                postImageRef.putFile(imageUri).addOnSuccessListener {
                    postImageRef.downloadUrl
                        .addOnSuccessListener {
                            val postUploadData1 = Post()
                            postUploadData1.postImage = it.toString()
                            postUploadData1.postDescription = binding.etPostDescription.text.toString()
                            postUploadData1.itemPrice = binding.itemPrice.text.toString()
                            postUploadData1.postedBy = auth.uid
                            database.reference
                                .child("buyAndSale")
                                .push()
                                .setValue(postUploadData1)
                                .addOnSuccessListener {
                                    dialog.dismiss()
                                    Toast.makeText(
                                        this@AskFavorActivity,
                                        "Posted Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                }
            }

            if (binding.btnSelectPostType.text.equals(options[2])) {
                Log.d("TAG112233", "uploading image ")

                dialog.show()
                val postImageRef = storage.reference
                    .child("lost_and_found")
                    .child(auth.uid!!)
                    .child(Date().time.toString())
                postImageRef.putFile(imageUri).addOnSuccessListener {
                    Log.d("TAG112233", "uploading image ")


                    postImageRef.downloadUrl
                        .addOnSuccessListener {

                            Log.d("TAG112233", "image link downlaoded: ")
                            val postUploadData2 = Post()
                            postUploadData2.postImage = it.toString()
                            postUploadData2.postedBy = auth.uid
                            postUploadData2.postDescription = binding.etPostDescription.text.toString()
                            postUploadData2.postTime = Date().time

                            database.reference
                                .child("lostAndFound")
                                .push()
                                .setValue(postUploadData2)
                                .addOnSuccessListener {
                                    Log.d("TAG112233", "post uploaded ")

                                    dialog.dismiss()
                                    Toast.makeText(
                                        this@AskFavorActivity,
                                        "Posted Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                }.addOnFailureListener{
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }else{

            //post has no image
        if (binding.btnSelectPostType.text.equals(options[0])) {
            dialog.show()

                        val postUploadData0 = Post()
                        postUploadData0.postImage = ""
                        postUploadData0.postedBy = auth.uid
                        postUploadData0.postDescription = binding.etPostDescription.text.toString()
                        postUploadData0.postTime = Date().time

                        val postID= database.reference
                            .child("favor").push().key
                        postUploadData0.postID=postID.toString()
                        database.reference
                            .child("favor")
                            .child(postID!!)
                            .setValue(postUploadData0)
                            .addOnSuccessListener {
                                dialog.dismiss()
                                Toast.makeText(
                                    this@AskFavorActivity,
                                    "Post posted Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val notification = Notification()
                                notification.notificationBy = FirebaseAuth.getInstance().uid
                                notification.notificationTime = Date().time
                                notification.postID = postUploadData0.postID
                                notification.postedBy = postUploadData0.postedBy
                                notification.notificationType = " favor"
                            }

            }

        if (binding.btnSelectPostType.text.equals(options[1])) {
            dialog.show()

                        val postUploadData1 = Post()
                        postUploadData1.postImage = ""
                        postUploadData1.postDescription = binding.etPostDescription.text.toString()
                        postUploadData1.itemPrice = binding.itemPrice.text.toString()
                        postUploadData1.postedBy = auth.uid
                        database.reference
                            .child("buyAndSale")
                            .push()
                            .setValue(postUploadData1)
                            .addOnSuccessListener {
                                dialog.dismiss()
                                Toast.makeText(
                                    this@AskFavorActivity,
                                    "Posted Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

            }


         if (binding.btnSelectPostType.text.equals(options[2])) {
             Log.d("TAG112233", "uploading image ")

             dialog.show()

                        Log.d("TAG112233", "image link downlaoded: ")
                        val postUploadData2 = Post()
                        postUploadData2.postImage = ""
                        postUploadData2.postedBy = auth.uid
                        postUploadData2.postDescription = binding.etPostDescription.text.toString()
                        postUploadData2.postTime = Date().time

                        database.reference
                            .child("lostAndFound")
                            .push()
                            .setValue(postUploadData2)
                            .addOnSuccessListener {
                                Log.d("TAG112233", "post uploaded ")

                                dialog.dismiss()
                                Toast.makeText(
                                    this@AskFavorActivity,
                                    "Posted Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

            }
        }

        }
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == image) {
                imageUri = data?.data!!
                binding.showImage.setImageURI(imageUri)

                binding.showImage.visibility = View.VISIBLE
                binding.btnPost.isEnabled = true
                binding.btnPost.background =
                    AppCompatResources.getDrawable(this@AskFavorActivity, R.drawable.post_button)
                binding.btnPost.setTextColor(resources.getColor(R.color.white))
            }
        }
    }
}
