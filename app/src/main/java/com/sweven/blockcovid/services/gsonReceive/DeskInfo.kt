package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.SerializedName

data class DeskInfo (
    val roomName: String,
    @SerializedName("deskId")
    val deskId: String,
    val x: Long,
    val y: Long,
    val status: String,
    val links: List<Links>
)
