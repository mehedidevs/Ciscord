package com.ciscord.talk.chat.hangout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Group(
    val groupName: String = "",
    val groupImage: String = "",
    val groupBio: String = "",
    val groupAdmin: String = "",
    val groupID: String = "",
    val groupMember: MutableList<String> = mutableListOf()
) : Parcelable

