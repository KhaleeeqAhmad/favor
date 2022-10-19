package com.fyp.favorproject.authenticationFragments


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.FragmentSignupBinding
import com.fyp.favorproject.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignupBinding
    private lateinit var database: FirebaseDatabase


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding = FragmentSignupBinding.inflate(inflater)

        binding.btnSignup.setOnClickListener() {
            var name = binding.etUserNameSignup.text.toString()
            var email = binding.etEmailSignup.text.toString().trim()
            var password = binding.etPasswordSignup.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val newUser = User(name, email, password)
                        val id = it.result.user?.uid as String
                        database.reference.child("User").child(id).setValue(newUser)
                        Toast.makeText(context, "UserDataSaved", Toast.LENGTH_SHORT).show()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", it.exception)
                        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        return binding.root
    }

//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//            reload()
//        }
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Buttons
//        with (binding) {
//            emailSignInButton.setOnClickListener {
//                val email = binding.fieldEmail.text.toString()
//                val password = binding.fieldPassword.text.toString()
//                signIn(email, password)
//            }
//            btnSignup.setOnClickListener {
//                val email = etEmailSignup.text.toString()
//                val password = etPasswordSignup.text.toString()
//                createAccount(email, password)
//            }
//        }
//
//        // Initialize Firebase Auth
//        auth = Firebase.auth
//    }
//
//    private fun updateUI(user: FirebaseUser?) {
//
//    }
//
//    private fun reload() {
//
//    }

}