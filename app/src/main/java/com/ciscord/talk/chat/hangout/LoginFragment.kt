package com.ciscord.talk.chat.hangout

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ciscord.talk.chat.hangout.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginFragment : Fragment() {


    lateinit var binding: FragmentLoginBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        FirebaseAuth.getInstance().currentUser?.let {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }



        binding.loginBtn.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()


            if (isEmailValid(email) && isPasswordValid(password)) {
                loginUser(email, password)
            } else {

                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT)
                    .show()
            }


        }

        binding.createNewAccount.setOnClickListener {

            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

        }





        return binding.root
    }

    private fun loginUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    Toast.makeText(context, "Login successful: ${user?.email}", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                } else {
                    // Login failed
                    Toast.makeText(
                        context,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }


    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
//        val passwordRegex =
//            Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
        return password.length >= 6
    }


}