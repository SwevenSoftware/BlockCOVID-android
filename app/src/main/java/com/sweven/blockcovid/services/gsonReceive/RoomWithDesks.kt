package com.sweven.blockcovid.services.gsonReceive

data class RoomWithDesks(
    val room: Room,
    val desks: List<Desk>,
    val links: List<Link>
)
