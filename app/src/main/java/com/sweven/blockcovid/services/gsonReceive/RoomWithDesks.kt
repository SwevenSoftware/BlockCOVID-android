package com.sweven.blockcovid.services.gsonReceive

data class RoomWithDesks (
        val room: Room,
        val desks: List<Desk>,
        val links: List<Link>
)

data class Desk (
        val id: Long,
        val x: Long,
        val y: Long
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
