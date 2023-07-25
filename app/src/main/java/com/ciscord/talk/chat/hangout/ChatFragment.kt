package com.ciscord.talk.chat.hangout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bumptech.glide.Glide
import com.ciscord.talk.chat.hangout.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID


class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var chatDb: DatabaseReference
    lateinit var userIDSelf: String
    lateinit var userIDRemote: String

    val chatList = mutableListOf<TextMessage>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)

        val layoutManger = LinearLayoutManager(requireContext())


        layoutManger.stackFromEnd= true

        binding.chatRcv.layoutManager = layoutManger





        requireArguments().getString(USERID)?.let {
            userIDRemote = it
        }

        chatDb = FirebaseDatabase.getInstance().reference

        FirebaseAuth.getInstance().currentUser?.let {
            userIDSelf = it.uid
        }
        chatDb.child(DBNODES.USER).child(userIDRemote).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                snapshot.getValue(User::class.java)?.let {


                    Glide.with(requireContext()).load(it.profileImage)
                        .placeholder(R.drawable.placeholder).into(binding.profileImage)

                    binding.nameTv.text = it.fullName

                    binding.emailTv.text = it.email


                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        messageToShow()



        return binding.root
    }

    private fun messageToShow() {

        chatDb.child(DBNODES.CHATS).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                chatList.clear()


                snapshot.children.forEach { snp ->
                    snp.getValue(TextMessage::class.java)?.let {

                        if (it.senderId == userIDSelf && it.receiver == userIDRemote
                            || it.senderId == userIDRemote && it.receiver == userIDSelf


                        ) {
                            chatList.add(it)

                        }


                    }


                }


                val adaApter = ChatAdaApter(userIDSelf, chatList)

                binding.chatRcv.adapter = adaApter


            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendBtn.setOnClickListener {


            val textMessage = TextMessage(
                text = binding.inputMsg.text.toString(),
                senderId = userIDSelf,
                receiver = userIDRemote,
                msgID = ""
            )

            sendTextMessage(textMessage)


        }


    }

    private fun sendTextMessage(textMessage: TextMessage) {

        val msgID = chatDb.push().key ?: UUID.randomUUID().toString()
        textMessage.msgID = msgID

        chatDb.child(DBNODES.CHATS).child(msgID).setValue(textMessage).addOnCompleteListener {


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


    companion object {
        const val USERID = "user_id_key"
    }
}