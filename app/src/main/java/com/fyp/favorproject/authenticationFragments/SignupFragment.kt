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

    private lateinit var view: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentSignupBinding.inflate(inflater)

        view.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_mainActivity)
        }
        view.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        return view.root
    }
}