package com.ciscord.talk.chat.hangout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ciscord.talk.chat.hangout.databinding.FragmentGroupsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID


class GroupsFragment : Fragment(), GroupAdapter.Listener {

    lateinit var binding: FragmentGroupsBinding
    lateinit var userDb: DatabaseReference

    val groupList = mutableListOf<Group>()
    lateinit var currentUSerID: String
    val groupMember = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        FirebaseAuth.getInstance().currentUser?.let {
            currentUSerID = it.uid

        }

        userDb = FirebaseDatabase.getInstance().reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getAllGroup()


    }

    private fun getAllGroup() {


        userDb.child(DBNODES.Group).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()

                snapshot.children.forEach {

                    it.getValue(Group::class.java)?.let { group ->

                        groupList.add(group)
                    }

                }


                val adapter = GroupAdapter(
                    groupList = groupList,
                    currentUserID = currentUSerID,
                    listener = this@GroupsFragment
                )
                binding.groupRCV.adapter = adapter


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun joinGroup(userID: String, groupID: String, groupMember: MutableList<String>) {

        val members: MutableSet<String> = groupMember.toMutableSet()
        members.add(userID)
        var newGroupMember = members.toMutableList()


        val groupMap: MutableMap<String, Any> = mutableMapOf()
        groupMap["groupMember"] = newGroupMember


        userDb.child(DBNODES.Group).child(groupID).updateChildren(groupMap).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "User Added!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
            }
            getAllGroup()

        }

    }

    override fun groupChat(group: Group) {

        val bundle = Bundle()
        bundle.putParcelable("key", group)

        findNavController().navigate(R.id.action_groupsFragment_to_groupChatFragment, bundle)


    }


}