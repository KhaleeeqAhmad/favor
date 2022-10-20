package com.fyp.favorproject.authenticationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.FragmentSignupBinding



class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater)

        binding.btnSignup.setOnClickListener {
         findNavController().navigate(R.id.action_signupFragment_to_mainActivity)
        }
        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        return binding.root
    }

}