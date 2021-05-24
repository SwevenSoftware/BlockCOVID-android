package com.sweven.blockcovid.services.gsonReceive

import com.google.gson.annotations.SerializedName

data class Reservations(
    @SerializedName("_embedded")
    val embedded: Embedded,

    @SerializedName("_links")
    val links: ReservationsLinks
)

data class Embedded(
    val reservationWithRoomList: List<Reservation>
)

data class Reservation(
    val id: String,

    @SerializedName("deskId")
    val deskID: String,

    val room: String,
    val username: String,
    val start: String,
    val end: String,
    val usageStart: String?,
    val usageEnd: String?,
    val deskCleaned: Boolean,
    val ended: Boolean,

    @SerializedName("_links")
    val links: ReservationLinks
)

data class ReservationLinks(
    @SerializedName("new_reservation")
    val newReservation: NewReservation,

    @SerializedName("modify_reservation")
    val modifyReservation: SelfReservations,

    @SerializedName("desk_status_reservation")
    val deskStatusReservation: SelfReservations,

    @SerializedName("delete_reservation")
    val deleteReservation: SelfReservations
)

data class SelfReservations(
    val href: String,
    val templated: Boolean
)

data class NewReservation(
    val href: String
)

data class ReservationsLinks(
    val self: SelfReservations
)
