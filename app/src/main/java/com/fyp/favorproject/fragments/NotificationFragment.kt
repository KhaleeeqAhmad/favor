package com.fyp.favorproject.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fyp.favorproject.R
import com.fyp.favorproject.adapter.NotificationAdapter
import com.fyp.favorproject.data.DataSource
import com.fyp.favorproject.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment(R.layout.fragment_notification) {
    private lateinit var binding: FragmentNotificationBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater)

        val myDataSet = DataSource().loadNotification()
        binding.recyclerViewNotifications.adapter = NotificationAdapter(this, myDataSet)
        binding.recyclerViewNotifications.setHasFixedSize(true)

        return binding.root
    }

}