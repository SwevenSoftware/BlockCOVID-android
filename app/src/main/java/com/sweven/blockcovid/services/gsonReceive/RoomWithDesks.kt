package com.sweven.blockcovid.services.gsonReceive

data class RoomWithDesks (
        val room: Room,
        val desks: List<Desk>,
        val links: List<Link>
)

data class Desk (
        val x: Int,
        val y: Int
)

data class Link (
        val rel: String,
        val href: String,
        val hreflang: String,
        val media: String,
        val title: String,
        val type: String,
        val deprecation: String,
        val profile: String,
        val name: String
)
