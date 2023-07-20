package com.ciscord.talk.chat.hangout

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.load

import com.ciscord.talk.chat.hangout.databinding.ItemGroupBinding

class GroupAdapter(
    var groupList: MutableList<Group>,
    var currentUserID: String,
    var listener: GroupAdapter.Listener
) :
    RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    interface Listener {

        fun joinGroup(
            userID: String,
            groupID: String,
            groupMember: MutableList<String>
        )

        fun groupChat(group: Group)


    }


    class GroupViewHolder(var binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {


        return GroupViewHolder(
            ItemGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groupList[position]

        Log.d("TAG", "onBindViewHolder:  ${currentUserID.containsUser(group.groupMember)}")



        if (!currentUserID.containsUser(group.groupMember)) {
            holder.binding.joinBtn.text = Join
        } else {
            holder.binding.joinBtn.text = Chat
        }

        holder.binding.joinBtn.setOnClickListener {
            if (holder.binding.joinBtn.text.toString() == Join) {
                listener.joinGroup(
                    currentUserID,
                    groupID = group.groupID,
                    groupMember = group.groupMember
                )

            } else {

                listener.groupChat(group)

            }


        }





        holder.binding.fullName.text = group.groupName
        holder.binding.groupBio.text = group.groupBio
        holder.binding.profileImage.load(group.groupImage)


    }


    companion object {
        const val Join = "Join Group"
        const val Chat = "Join Chat"


    }

    private fun String.containsUser(keywords: MutableList<String>): Boolean {
        for (keyword in keywords) {
            if (this.contains(keyword, true)) return true
        }
        return false
    }


}