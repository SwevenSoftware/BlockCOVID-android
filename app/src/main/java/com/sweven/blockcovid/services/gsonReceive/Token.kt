package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Token {
    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("expiryDate")
    @Expose
    var expiryDate: String? = null

    @SerializedName("links")
    @Expose
    var links: List<TokenLink>? = null
}