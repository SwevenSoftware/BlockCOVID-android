package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.SerializedName

data class Reservations (
        val links: List<Link>,
        val content: List<Reservation>
)

data class Reservation (
        val id: String,
        val username: String,

        @SerializedName("deskID")
        val deskID: String,
        val start: String,
        val end: String,
        val room: String,
        val links: List<Link>
)
