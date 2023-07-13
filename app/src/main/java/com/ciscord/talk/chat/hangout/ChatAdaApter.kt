package com.ciscord.talk.chat.hangout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChatAdaApter(var userIDSelf: String) :
    ListAdapter<TextMessage, ChatAdaApter.ChatViewHolder>(COMPARATOR) {


    val LEFT: Int = 1
    val RIGHT: Int = 2

    val chatList = mutableListOf<TextMessage>()


    class ChatViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageTV: TextView = itemView.findViewById(R.id.chatTv)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {


        if (viewType == RIGHT) {

            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_right_chat, parent, false)

            return ChatViewHolder(view)
        } else {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_left_chat, parent, false)

            return ChatViewHolder(view)

        }


    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        getItem(position).apply {
            chatList.add(this)

            holder.messageTV.text = this.text
        }


    }


    override fun getItemViewType(position: Int): Int {

        return if (chatList[position].senderId == userIDSelf) {

            RIGHT
        } else {

            LEFT
        }


    }

    companion object {
        var COMPARATOR = object : DiffUtil.ItemCallback<TextMessage>() {
            override fun areItemsTheSame(oldItem: TextMessage, newItem: TextMessage): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TextMessage, newItem: TextMessage): Boolean {
                return oldItem.msgID == newItem.msgID
            }
        }


    }


}