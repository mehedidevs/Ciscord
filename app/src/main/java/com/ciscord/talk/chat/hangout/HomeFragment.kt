package com.ciscord.talk.chat.hangout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ciscord.talk.chat.hangout.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), UserAdapter.UserListener {

    lateinit var binding: FragmentHomeBinding
    lateinit var userDb: DatabaseReference
    lateinit var adapter: UserAdapter

    val userList: MutableList<User> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        userDb = FirebaseDatabase.getInstance().reference
        binding.logoutBtn.setOnClickListener {
            val auth = FirebaseAuth.getInstance()


            auth.signOut().apply {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)


            }


        }
        adapter = UserAdapter(this@HomeFragment)

        binding.userRcv.adapter = adapter



        getAllAvailableUser()




        return binding.root
    }

    private fun getAllAvailableUser() {

        userDb.child(DBNODES.USER).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                    userList.clear()
                    snapshot.children.forEach {

                        val user: User = it.getValue(User::class.java)!!

                        userList.add(user)


                    }

                    adapter.submitList(userList)


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }

    override fun userItemClick(user: User) {

        var bundle = Bundle()

        bundle.putString(ProfileFragment.USERID, user.userId)


        findNavController().navigate(R.id.action_homeFragment_to_profileFragment, bundle)


    }


}