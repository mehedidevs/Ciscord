package com.ciscord.talk.chat.hangout


data class GroupTextMessage(
    var text: String? = null,
    val senderId: String = "",
    val receiver: List<String> = emptyList(),
    var msgID: String = ""
)