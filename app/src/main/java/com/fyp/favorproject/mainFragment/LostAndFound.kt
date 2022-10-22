package com.fyp.favorproject.mainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.FragmentHomeBinding
import com.fyp.favorproject.databinding.FragmentLostAndFoundBinding


class LostAndFound : Fragment() {

    private lateinit var mBinding: FragmentLostAndFoundBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentLostAndFoundBinding.inflate(inflater)

        return mBinding.root
    }
}