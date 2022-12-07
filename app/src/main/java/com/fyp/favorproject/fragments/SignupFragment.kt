package com.fyp.favorproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
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
    private var fullEmail = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        navigationControl()


        binding.userNameUR.doOnTextChanged { _, _, _, _ ->
            if (binding.userNameUR.text?.contains(Regex("[^A-Za-z ]")) == true) {
                binding.etUserNameUserRegister.error = "Please use Alphabets"
                binding.btnSignUpUserRegister.isEnabled = false
            } else if (binding.userNameUR.text?.contains(Regex("[^A-Za-z ]")) == false) {
                binding.etUserNameUserRegister.error = null
                binding.btnSignUpUserRegister.isEnabled = true
            }
        }

        binding.etDiscipline.doOnTextChanged { _, _, _, _ ->

            createEmailListener()

        }
        binding.etYear.doOnTextChanged { _, _, _, _ ->

            createEmailListener()

        }
        binding.etRegNum.doOnTextChanged { _, _, _, _ ->

            if (binding.etRegNum.text.toString().length == 3) {


                if (binding.etRegNum.text?.contains("^[0-9]".toRegex()) == false) {

                    binding.etRegNumUserRegister.error = "Not Valid"
                    binding.btnSignUpUserRegister.isEnabled = false


                } else if (binding.etRegNum.text?.contains("^[0-9]".toRegex()) == true) {
                    binding.etRegNumUserRegister.error = null
                    binding.btnSignUpUserRegister.isEnabled = true
                    createEmailListener()
                }
            } else {
                binding.etRegNumUserRegister.error = "Not Valid"
                binding.btnSignUpUserRegister.isEnabled = false

            }

        }

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
        val email = fullEmail + getString(R.string.suffix)
        val password = binding.passwordRU.text.toString()

        if (name.isNotEmpty() && department.isNotEmpty() &&
            email.isNotEmpty() && password.isNotEmpty()
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    if (email.endsWith("@students.cuisahiwal.edu.pk")) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { accountCreated ->
                                if (accountCreated.isSuccessful) {
                                    val user = User(name, department, email, password)
                                    val id = accountCreated.result.user?.uid
                                    database.reference.child("User").child(id!!).setValue(user)
                                }
                            }.await()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "User data saved!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Please Enter CUI Email!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Authentication failed!", Toast.LENGTH_SHORT).show()
                        Log.d("EE", "registerUser: ${e.message} ")
                    }
                }
            }

        } else Toast.makeText(context, "Empty credentials!", Toast.LENGTH_SHORT).show()
    }


    override fun onResume() {
        super.onResume()

        val departments = resources.getStringArray(R.array.Departments)
        val departmentAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, departments)
        val batch = resources.getStringArray(R.array.Year)
        val batchAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, batch)

        val discipline = resources.getStringArray(R.array.Discipline)
        val disciplineAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, discipline)

        binding.etDepartment.setAdapter(departmentAdapter)



        binding.etYear.setAdapter(batchAdapter)


        binding.etDiscipline.setAdapter(disciplineAdapter)

    }

    private fun createEmailListener() {

        fullEmail =
            binding.etYear.text.toString() + "-" + binding.etDiscipline.text.toString() + "-" + binding.etRegNum.text.toString()
        binding.emailRU.setText(fullEmail)
        fullEmail = binding.emailRU.text.toString()
    }


}
