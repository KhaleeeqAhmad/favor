package com.fyp.favorproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.favorproject.adapter.BuySaleAdapter
import com.fyp.favorproject.databinding.FragmentBuySaleBinding
import com.fyp.favorproject.mainFragment.ChattingActivity
import com.fyp.favorproject.model.Post
import com.fyp.favorproject.utill.ResponseInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class BuySaleFragment : Fragment(), ResponseInterface  {
    private lateinit var binding: FragmentBuySaleBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var buySaleList: ArrayList<Post>
        private lateinit var currentInstace: BuySaleFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBuySaleBinding.inflate(layoutInflater)

        currentInstace= this
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        //RecyclerView
        val recyclerView = binding.recyclerViewBuySale
        recyclerView.layoutManager = LinearLayoutManager(context).apply {reverseLayout = true}.apply { stackFromEnd = true}

        recyclerView.setHasFixedSize(true)

        buySaleList = ArrayList()

        getPostData()


        return binding.root
    }



    private fun getPostData() {
        binding.recyclerViewBuySale.visibility = View.GONE
        val postData = database.reference.child("buyAndSale")
        postData.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (postSnaps in snapshot.children) {
                        val post = postSnaps.getValue(Post::class.java)
                        buySaleList.add(post!!)
                    }
                }
                val userAdapter = BuySaleAdapter(currentInstace, buySaleList, currentInstace)
                binding.recyclerViewBuySale.adapter = userAdapter
                binding.recyclerViewBuySale.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) = Unit
        })
    }

    override fun clickResponse(uid: String) {
        if (uid == FirebaseAuth.getInstance().uid!!){
            return
        }
        val intent = Intent(requireContext(), ChattingActivity::class.java).apply {
            putExtra("friendUID", uid)
        }
        startActivity(intent)
    }
}