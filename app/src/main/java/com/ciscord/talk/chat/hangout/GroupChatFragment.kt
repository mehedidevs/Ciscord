package com.ciscord.talk.chat.hangout

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ciscord.talk.chat.hangout.databinding.FragmentGroupChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID


class GroupChatFragment : Fragment() {

    lateinit var userIDSelf: String
    lateinit var binding: FragmentGroupChatBinding

    lateinit var group: Group
    lateinit var chatDb: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentGroupChatBinding.inflate(inflater, container, false)
        group = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable<Group>("key", Group::class.java)!!
        } else {
            requireArguments().getParcelable<Group>("key")!!
        }



        group?.let {
            Log.d("group", "$it ")
        }


        val layoutManger = LinearLayoutManager(requireContext())


        layoutManger.stackFromEnd = true

        binding.chatRcv.layoutManager = layoutManger


        chatDb = FirebaseDatabase.getInstance().reference

        FirebaseAuth.getInstance().currentUser?.let {
            userIDSelf = it.uid
        }
        chatDb.child(DBNODES.Group).child(group.groupID).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                snapshot.getValue(Group::class.java)?.let {


                    Glide.with(requireContext()).load(it.groupImage)
                        .placeholder(R.drawable.placeholder).into(binding.profileImage)

                    binding.nameTv.text = it.groupName

                    //binding.emailTv.text = it.groupName


                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.sendBtn.setOnClickListener {


            val textMessage = GroupTextMessage(
                text = binding.inputMsg.text.toString(),
                senderId = userIDSelf,
                receiver = group.groupMember,
                msgID = ""
            )

            sendTextMessage(textMessage)


        }

    }

    private fun sendTextMessage(textMessage: GroupTextMessage) {

        val msgID = chatDb.push().key ?: UUID.randomUUID().toString()
        textMessage.msgID = msgID

        chatDb.child(DBNODES.GROUP_CHATS).child(group.groupID).child(msgID).setValue(textMessage).addOnCompleteListener {


            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Message Sent", Toast.LENGTH_SHORT).show()
                binding.inputMsg.setText("")

            } else {
                Toast.makeText(
                    requireContext(),
                    it.exception?.message ?: "Something went Wrong!",
                    Toast.LENGTH_SHORT
                ).show()

            }


        }


    }


}