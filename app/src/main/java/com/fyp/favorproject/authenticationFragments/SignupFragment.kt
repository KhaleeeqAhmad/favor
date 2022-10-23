package com.fyp.favorproject.authenticationFragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fyp.favorproject.R
import com.fyp.favorproject.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignupBinding
    private lateinit var database: FirebaseDatabase




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding = FragmentSignupBinding.inflate(inflater)

        isUserAlreadyLoggedIn()

        binding.btnSignup.setOnClickListener {
           if(binding.etUserNameSignup.text.toString().isBlank() ||
               binding.etEmailSignup.text.toString().isBlank() ||
               binding.etPasswordSignup.text.toString().isBlank()) {
               Toast.makeText(context , "Empty credentials", Toast.LENGTH_SHORT)
                   .show()
           } else {
               signUp()
           }
        }
        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        return binding.root
    }
    private fun signUp() {
        val name = binding.etUserNameSignup.text.toString()
        val email = binding.etEmailSignup.text.toString().trim()
        val password = binding.etPasswordSignup.text.toString()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            if (it.isSuccessful) {
                val newUser = com.fyp.favorproject.userData.User(name, email, password)
                val id = it.result.user?.uid as String
                database.reference.child("User").child(id).setValue(newUser)
                Toast.makeText(context, "UserDataSaved", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_signupFragment_to_mainActivity)
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    private fun isUserAlreadyLoggedIn() {
        if (FirebaseAuth.getInstance().currentUser != null){
            findNavController().navigate(R.id.action_signupFragment_to_mainActivity)
        }
    }
}
