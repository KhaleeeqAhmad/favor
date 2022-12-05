package com.fyp.favorproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.favorproject.adapter.LostAndFoundAdapter
import com.fyp.favorproject.databinding.FragmentLostFoundBinding
import com.fyp.favorproject.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class LostFoundFragment : Fragment() {
    private lateinit var binding: FragmentLostFoundBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var lostFoundList: ArrayList<Post>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLostFoundBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        //RecyclerView
        val recyclerView = binding.recyclerViewLostFound
        recyclerView.layoutManager = LinearLayoutManager(context).apply {reverseLayout = true}.apply { stackFromEnd = true}
        recyclerView.setHasFixedSize(true)

        lostFoundList = ArrayList()

        getPostData()

        return binding.root
    }

    private fun getPostData() {
        binding.recyclerViewLostFound.visibility = View.GONE

        val postData = database.reference.child("lostAndFound")
        postData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (postSnaps in snapshot.children) {
                        val post = postSnaps.getValue(Post::class.java)
                        lostFoundList.add(post!!)
                    }
                }
                val userAdapter = LostAndFoundAdapter(context = LostFoundFragment(), lostFoundList)
                binding.recyclerViewLostFound.adapter = userAdapter
                binding.recyclerViewLostFound.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }
}