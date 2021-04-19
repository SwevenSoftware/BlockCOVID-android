package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.SerializedName

data class Reservations (
        @SerializedName("_embedded")
        val embedded: EmbeddedReservations,

        @SerializedName("_links")
        val links: Links
)

data class EmbeddedReservations (
        val reservationList: List<Reservation>
)

data class Reservation (
        val id: String,
        val username: String,

        @SerializedName("deskId")
        val deskID: String,

        val start: String,
        val end: String,

        @SerializedName("_links")
        val links: ReservationListLinks
)

data class ReservationListLinks (
        @SerializedName("new_reservation")
        val newReservation: NewReservation,

        @SerializedName("modify_reservation")
        val modifyReservation: SelfRooms,

        @SerializedName("desk_status_reservation")
        val deskStatusReservation: SelfRooms,

        @SerializedName("delete_reservation")
        val deleteReservation: SelfRooms
)

data class SelfRooms (
        val href: String,
        val templated: Boolean
)

data class NewReservation (
        val href: String
)
