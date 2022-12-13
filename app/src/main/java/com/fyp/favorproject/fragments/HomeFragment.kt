package com.fyp.favorproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.favorproject.adapter.FavorAdapter
import com.fyp.favorproject.databinding.FragmentHomeBinding
import com.fyp.favorproject.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var postList: ArrayList<Post>
    private lateinit var currentInstance : HomeFragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()



        //RecyclerView
        val recyclerView = binding.recyclerViewPosts
        recyclerView.layoutManager = LinearLayoutManager(context).apply {reverseLayout = true}.apply { stackFromEnd = true}
        recyclerView.setHasFixedSize(true)

        postList = ArrayList()

        HomeFragment()
        getPostData()
        currentInstance= this
        return binding.root
    }



    private fun getPostData() {
        binding.recyclerViewPosts.visibility = View.GONE

        val postData = database.reference.child("favor")
        postData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                if (snapshot.exists()) {
                    for (postSnaps in snapshot.children) {
                        val post = postSnaps.getValue(Post::class.java)
                        post?.postID = postSnaps.key.toString()
                        postList.add(post!!)
                    }
                }
                val userAdapter = FavorAdapter(currentInstance, postList)
                binding.recyclerViewPosts.adapter = userAdapter
                binding.recyclerViewPosts.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

}