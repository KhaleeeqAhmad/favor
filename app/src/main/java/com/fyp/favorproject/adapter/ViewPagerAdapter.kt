package com.fyp.favorproject.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fyp.favorproject.fragments.BuySaleFragment
import com.fyp.favorproject.fragments.LostFoundFragment


class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter (fragmentActivity){

    override fun getItemCount() = 2


    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {BuySaleFragment()}
            1 -> {LostFoundFragment()}
            else -> {throw Resources.NotFoundException("Fragment Not Found")}
        }
    }


}