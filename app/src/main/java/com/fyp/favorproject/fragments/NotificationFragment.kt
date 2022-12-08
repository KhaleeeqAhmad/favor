package com.fyp.favorproject.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.favorproject.R
import com.fyp.favorproject.adapter.NotificationAdapter
import com.fyp.favorproject.databinding.FragmentNotificationBinding
import com.fyp.favorproject.model.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage


class NotificationFragment : Fragment(R.layout.fragment_notification) {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var notificationList: ArrayList<Notification>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()


        //RecyclerView
        val recyclerView = binding.recyclerViewNotifications
        recyclerView.layoutManager = LinearLayoutManager(context).apply {reverseLayout = true}.apply { stackFromEnd = true}
        recyclerView.setHasFixedSize(true)

        notificationList = ArrayList()
        getNotificationData()

        return binding.root
    }

    private fun getNotificationData() {
        val  notificationRef = database.reference.child("notification").child(auth.uid!!)
        notificationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val notification = dataSnapshot.getValue<Notification>()
                    notification?.notificationID = dataSnapshot.key
                    notificationList.add(notification!!)
                }
                val userAdapter = NotificationAdapter(context = NotificationFragment(), notificationList)
                binding.recyclerViewNotifications.adapter = userAdapter
                binding.recyclerViewNotifications.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) = Unit

        })
    }

}