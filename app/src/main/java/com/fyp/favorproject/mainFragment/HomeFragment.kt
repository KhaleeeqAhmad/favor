package com.fyp.favorproject.mainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fyp.favorproject.R
import com.fyp.favorproject.adapter.DashboardAdapter
import com.fyp.favorproject.data.Datasource
import com.fyp.favorproject.databinding.FragmentHomeBinding


class HomeFragment : Fragment(R.layout.fragment_home){

    private lateinit var mBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater)

        val myDataSet = Datasource().loadPosts()
        mBinding.recyclerViewPosts.adapter = DashboardAdapter(this,myDataSet)
        mBinding.recyclerViewPosts.setHasFixedSize(true)


        return mBinding.root
    }


}
