package com.fyp.favorproject.fragments
import android.os.Bundle
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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        navigationControl()

        return binding.root
    }

    private fun navigationControl() {
        binding.btnSignUpUserRegister.setOnClickListener {
            registerUser()
        }
        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

    private fun registerUser() {
        val name = binding.userNameUR.text.toString()
        val department = binding.etDepartment.text.toString()
        val regNo = binding.etRegNo.text.toString()
        val email = binding.emailRU.text.toString()
        val password = binding.passwordRU.text.toString()

        if (name.isNotEmpty() && department.isNotEmpty() && regNo.isNotEmpty() &&
            email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { accountCreated ->
                        if (accountCreated.isSuccessful) {
                            val user = User(name, department, regNo, email, password)
                            val id = accountCreated.result.user?.uid
                            database.reference.child("User").child(id!!).setValue(user)
                        }
                    }.await()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "User data saved!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signupFragment_to_mainActivity)
                        activity?.finish()
                    }
                } catch (e:Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Authentication failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else Toast.makeText(context, "Empty credentials!", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            findNavController().navigate(R.id.action_signupFragment_to_mainActivity)
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}