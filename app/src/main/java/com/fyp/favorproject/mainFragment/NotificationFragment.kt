package com.fyp.favorproject.mainFragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.FragmentHomeBinding


class NotificationFragment : Fragment(R.layout.fragment_notification) {
    private lateinit var mBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater)






        return mBinding.root
    }

}