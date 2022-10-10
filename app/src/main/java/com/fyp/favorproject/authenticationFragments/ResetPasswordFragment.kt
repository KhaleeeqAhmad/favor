package com.fyp.favorproject.authenticationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment() {
    private lateinit var view: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentResetPasswordBinding.inflate(inflater)

        view.btnResetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }
            return view.root
    }
}