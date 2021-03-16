package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("id")
    var id: String,

    @SerializedName("expiryDate")
    var expiryDate: String,

    @SerializedName("username")
    var username: String
)