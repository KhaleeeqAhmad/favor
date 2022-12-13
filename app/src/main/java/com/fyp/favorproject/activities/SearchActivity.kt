package com.fyp.favorproject.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.favorproject.adapter.SearchUserAdapter
import com.fyp.favorproject.databinding.ActivitySearchBinding
import com.fyp.favorproject.mainFragment.ChattingActivity
import com.fyp.favorproject.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var userList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        val recyclerView = binding.rvSearchUser
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userList = arrayListOf()

        getUsers()
    }

    private fun getUsers() {
        binding.rvSearchUser.visibility = View.GONE
        val usersDataRef = database.getReference("User")

        Log.d("AAAAAA", "getUsers:  1")
        usersDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("AAAAAA", "getUsers:  2")
                userList.clear()
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val userData = userSnap.getValue(User::class.java)
                        userData!!.userID = userSnap.key
                        if (!userSnap.key.equals(FirebaseAuth.getInstance().uid)) {
                            userList.add(userData)
                        }
                    }
                    val userAdapter = SearchUserAdapter(userList, messageListener)
                    binding.rvSearchUser.adapter = userAdapter
                    binding.rvSearchUser.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError)= Unit

        })
        Log.d("AAAAAA", "getUsers:  3")
    }
    val messageListener = fun(friendUID: String){


            if (friendUID == FirebaseAuth.getInstance().uid!!){
                Toast.makeText(this, "You can't Message this user", Toast.LENGTH_SHORT).show()
                return
            }
            val intent = Intent(this, ChattingActivity::class.java).apply {
                putExtra("friendUID", friendUID)
            }
            startActivity(intent)

    }

}