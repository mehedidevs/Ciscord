package com.ciscord.talk.chat.hangout

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ciscord.talk.chat.hangout.databinding.FragmentGroupChatBinding


class GroupChatFragment : Fragment() {

    lateinit var binding: FragmentGroupChatBinding

    lateinit var group: Group

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


        return binding.root
    }


}