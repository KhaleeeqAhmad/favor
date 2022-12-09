package com.fyp.favorproject.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.ActivityMainBinding
import com.fyp.favorproject.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        setupNavigation()
    }

    // Bottom Navigation Setup.
    @SuppressLint("SetTextI18n")
    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentHolder) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.askFavorActivity) {
                val intent = Intent(this@MainActivity, AskFavorActivity::class.java)
                startActivity(intent)
            }
            if (destination.id == R.id.storeFragment) {
                binding.searchViewHome.visibility = View.GONE
                binding.fragmentName.visibility = View.VISIBLE
                binding.fragmentName.text = "Store"
            }
            if (destination.id == R.id.notificationFragment) {
                binding.searchViewHome.visibility = View.GONE
                binding.fragmentName.visibility = View.VISIBLE
                binding.fragmentName.text = "Notifications"
            }
            if (destination.id == R.id.homeFragment) {
                binding.searchViewHome.visibility = View.VISIBLE
                binding.fragmentName.visibility = View.GONE
            }
            if (destination.id == R.id.chatFragment) {
                binding.searchViewHome.visibility = View.GONE
                binding.fragmentName.visibility = View.VISIBLE
                binding.fragmentName.text = "Chats"
            }
            binding.appBarHome.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btnLogout -> {
                        auth.signOut()
                        this@MainActivity.finish()
                        val intent = Intent(this@MainActivity, AuthenticationActivity::class.java)
                        startActivity(intent)

                        true
                    }
                    else -> false
                }
            }
            binding.btnGotoUserProfile.setOnClickListener {
                val intent = Intent(this@MainActivity, UserProfileActivity::class.java)
                startActivity(intent)
            }

            binding.searchViewHome.setOnClickListener {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val profilePhoto = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>()
                    Picasso.get()
                        .load(user?.userProfilePhoto)
                        .placeholder(R.drawable.place_holder_image)
                        .into(binding.btnGotoUserProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) = Unit
        }
        database.reference.child("User").child(auth.uid!!).addValueEventListener(profilePhoto)
    }
}

