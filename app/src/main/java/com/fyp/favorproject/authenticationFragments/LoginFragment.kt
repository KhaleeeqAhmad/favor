package com.fyp.favorproject.authenticationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var view: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentLoginBinding.inflate(layoutInflater)

        view.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
        }
        view.btnForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
        view.btnGotoSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        return view.root
    }


}