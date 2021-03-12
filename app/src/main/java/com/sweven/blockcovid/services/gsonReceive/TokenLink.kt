package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TokenLink {
    @SerializedName("rel")
    @Expose
    var rel: String? = null

    @SerializedName("href")
    @Expose
    var href: String? = null

    @SerializedName("hreflang")
    @Expose
    var hreflang: String? = null

    @SerializedName("media")
    @Expose
    var media: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("deprecation")
    @Expose
    var deprecation: String? = null

    @SerializedName("profile")
    @Expose
    var profile: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
}