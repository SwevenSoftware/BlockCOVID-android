package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.SerializedName


data class Rooms (
        @SerializedName("_embedded")
        val embedded: Embedded,

        @SerializedName("_links")
        val links: Welcome5Links
)

data class Embedded (
        val roomList: List<RoomList>
)

data class RoomList (
        val name: String,
        val closed: Boolean,
        val openingTime: String,
        val closingTime: String,
        val openingDays: List<String>,
        val width: Long,
        val height: Long,

        @SerializedName("_links")
        val links: RoomListLinks
)

data class RoomListLinks (
        val self: Self,

        @SerializedName("new_room")
        val newRoom: Self,

        @SerializedName("list_rooms")
        val listRooms: Self
)

data class Self (
        val href: String
)

data class Welcome5Links (
        val self: Self
)
