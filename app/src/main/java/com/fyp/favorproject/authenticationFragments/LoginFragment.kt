package com.fyp.favorproject.authenticationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(layoutInflater)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            if(binding.etLoginEmail.text.toString().isBlank() ||
                binding.etLoginPassword.text.toString().isBlank()) {
                Toast.makeText(context , "Empty credentials", Toast.LENGTH_SHORT)
                    .show()
            } else {
                login()
            }

        }
        binding.btnForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
        binding.btnGotoSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        return binding.root
    }

    private fun login() {
        val email = binding.etLoginEmail.text.toString()
        val password = binding.etLoginPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT)
                        .show()
                }else {
                    Toast.makeText(context, "Please check your email & password and try again!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }


}