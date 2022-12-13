package com.fyp.favorproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fyp.favorproject.databinding.FragmentResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater)
        val auth = FirebaseAuth.getInstance()

        binding.btnSendCode.setOnClickListener {
           // findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)

            auth.currentUser!!.updatePassword("")

        }
            return binding.root
    }
}