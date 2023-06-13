package com.ciscord.talk.chat.hangout

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ciscord.talk.chat.hangout.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterFragment : Fragment() {


    lateinit var binding: FragmentRegisterBinding

    lateinit var userDb: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)


        userDb = FirebaseDatabase.getInstance().reference

        binding.registerBtn.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val name = binding.etName.text.toString().trim()


            if (isEmailValid(email) && isPasswordValid(password)) {
                registerUser(email, password, name)
            } else {

                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT)
                    .show()
            }

        }



        return binding.root
    }

    private fun registerUser(email: String, password: String, name: String) {
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                saveUserToDataBase(auth.currentUser?.uid, email, name)


            } else {

                Toast.makeText(
                    requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT
                ).show()

            }


        }


    }

    private fun saveUserToDataBase(uid: String?, email: String, name: String) {

        uid?.let {
            val user = User(
                userId = uid, email = email, fullName = name
            )


            userDb.child(DBNODES.USER).child(it).setValue(user).addOnCompleteListener { task ->


                if (task.isSuccessful) {

                    findNavController().navigate(R.id.action_registerFragment_to_homeFragment)

                } else {

                    Toast.makeText(
                        requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT
                    ).show()


                }


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