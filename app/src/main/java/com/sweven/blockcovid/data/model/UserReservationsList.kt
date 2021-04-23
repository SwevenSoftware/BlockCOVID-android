package com.sweven.blockcovid.data.model

data class UserReservationsList (
    val reservationId: Array<String>?,
    val deskId: Array<String>?,
    val room: Array<String>?,
    val startTime: Array<String>?,
    val endTime: Array<String>?,
    val day: Array<String>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserReservationsList

        if (reservationId != null) {
            if (other.reservationId == null) return false
            if (!reservationId.contentEquals(other.reservationId)) return false
        } else if (other.reservationId != null) return false
        if (deskId != null) {
            if (other.deskId == null) return false
            if (!deskId.contentEquals(other.deskId)) return false
        } else if (other.deskId != null) return false
        if (room != null) {
            if (other.room == null) return false
            if (!room.contentEquals(other.room)) return false
        } else if (other.room != null) return false
        if (startTime != null) {
            if (other.startTime == null) return false
            if (!startTime.contentEquals(other.startTime)) return false
        } else if (other.startTime != null) return false
        if (endTime != null) {
            if (other.endTime == null) return false
            if (!endTime.contentEquals(other.endTime)) return false
        } else if (other.endTime != null) return false
        if (day != null) {
            if (other.day == null) return false
            if (!day.contentEquals(other.day)) return false
        } else if (other.day != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = reservationId?.contentHashCode() ?: 0
        result = 31 * result + (deskId?.contentHashCode() ?: 0)
        result = 31 * result + (room?.contentHashCode() ?: 0)
        result = 31 * result + (startTime?.contentHashCode() ?: 0)
        result = 31 * result + (endTime?.contentHashCode() ?: 0)
        result = 31 * result + (day?.contentHashCode() ?: 0)
        return result
    }
}