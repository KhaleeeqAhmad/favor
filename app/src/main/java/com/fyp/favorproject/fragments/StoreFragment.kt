@file:Suppress("DEPRECATION")

package com.fyp.favorproject.fragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.fyp.favorproject.R
import com.fyp.favorproject.adapter.ViewPagerAdapter
import com.fyp.favorproject.databinding.FragmentStoreBinding
import com.google.android.material.tabs.TabLayoutMediator

class StoreFragment : Fragment(R.layout.fragment_store) {
    private lateinit var binding: FragmentStoreBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoreBinding.inflate(layoutInflater)

        binding.viewPager.adapter = ViewPagerAdapter(context as FragmentActivity)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, index ->
            tab.text = when (index) {
                0 -> {"Buy & Sale"}
                1 -> {"Lost & Found"}
                else -> {throw Resources.NotFoundException("Fragment not found")}
            }
        }.attach()

        return binding.root
    }



}