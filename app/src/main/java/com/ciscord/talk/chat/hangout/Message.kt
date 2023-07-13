package com.ciscord.talk.chat.hangout


interface Message {

    val senderId: String
    val receiver: String
    var msgID: String
}


data class TextMessage(
    var text: String? = null,
    override val senderId: String = "",
    override val receiver: String = "",
    override var msgID: String = ""
) : Message

data class ImageMessage(
    val imageLink: String = "",
    override val senderId: String = "",
    override val receiver: String = "",
    override var msgID: String = ""
) : Message