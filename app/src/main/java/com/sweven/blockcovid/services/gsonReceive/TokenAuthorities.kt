package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.SerializedName

data class TokenAuthorities (

        val token: Token,

        @SerializedName("authorities")
        val authoritiesList: List<String>
)


