package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.SerializedName

data class Rooms (
        @SerializedName("_embedded")
        val embedded: Embedded,

        @SerializedName("_links")
        val links: RoomsLinks
)

data class Embedded (
        val roomWithDesksList: List<RoomWithDesksList>
)

data class RoomWithDesksList (
        val room: Room,
        val desks: List<Any?>,

        @SerializedName("_links")
        val links: RoomWithDesksListLinks
)

data class RoomWithDesksListLinks (
        val self: Self,

        @SerializedName("new_room")
        val newRoom: Self,

        @SerializedName("list_rooms")
        val listRooms: Self
)

data class Self (
        val href: String
)

data class Room (
        val name: String,
        val closed: Boolean,
        val openingTime: String,
        val closingTime: String,
        val openingDays: List<String>,
        val width: Long,
        val height: Long
)

data class RoomsLinks (
        val self: Self
)
