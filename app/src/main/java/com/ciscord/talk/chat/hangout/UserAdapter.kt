package com.ciscord.talk.chat.hangout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ciscord.talk.chat.hangout.databinding.ItemUserBinding

class UserAdapter(val userListener: UserListener) :
    ListAdapter<User, UserAdapter.UserViewHolder>(COMPARATOR) {


    interface UserListener {

        fun userItemClick(user: User)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {


        getItem(position)?.let {
            holder.binding.apply {
                fullName.text = it.fullName
                userBio.text = it.bio
                email.text = it.email

                holder.itemView.setOnClickListener { _ ->

                    userListener.userItemClick(it)

                }
            }


        }
    }


    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.userId == newItem.userId
            }

        }


    }

    class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)


}