package com.ciscord.talk.chat.hangout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ciscord.talk.chat.hangout.databinding.FragmentProfileEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileEditFragment : Fragment() {


    lateinit var binding: FragmentProfileEditBinding

    lateinit var userDb: DatabaseReference


    private var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileEditBinding.inflate(layoutInflater)


        userDb = FirebaseDatabase.getInstance().reference




        requireArguments().getString(USERID)?.let {
            userId = it
            getUserByID(it)
        }



        binding.saveBtn.setOnClickListener {
            val userMap: MutableMap<String, Any> = mutableMapOf()
            userMap["fullName"] = binding.fullName.text.toString().trim()
            userMap["bio"] = binding.bio.text.toString().trim()


            userDb.child(DBNODES.USER).child(userId).updateChildren(userMap).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Profile Updated ! ", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }







        return binding.root
    }

    companion object {
        const val USERID = "user_id_key"
    }

    private fun getUserByID(userId: String) {


        userDb.child(DBNODES.USER).child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                snapshot.getValue(User::class.java)?.let {

                    binding.userEmail.text = it.email
                    binding.bio.setText(it.bio)
                    binding.fullName.setText(it.fullName)


                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }


}