package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.SerializedName

data class Desk (
    @SerializedName("deskId")
    val deskId: String,
    val x: Long,
    val y: Long,
    val available: Boolean
)
