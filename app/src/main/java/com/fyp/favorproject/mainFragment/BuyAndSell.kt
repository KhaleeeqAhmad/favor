package com.fyp.favorproject.mainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.FragmentBuyAndSellBinding
import com.fyp.favorproject.databinding.FragmentHomeBinding


class BuyAndSell : Fragment() {

    private lateinit var mBinding: FragmentBuyAndSellBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentBuyAndSellBinding.inflate(inflater)

        return mBinding.root
    }
}