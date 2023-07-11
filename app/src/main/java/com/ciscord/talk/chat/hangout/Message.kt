package com.ciscord.talk.chat.hangout

data class Message(
    val msg: String = "",
    val senderId: String = "",
    val receiver: String = "",
    val msgID: String = ""
)