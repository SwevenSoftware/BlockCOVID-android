package com.sweven.blockcovid.ui.cleanerRooms

data class RoomsList (
    val roomName: Array<String>?,
    val roomIsCleaned: Array<Boolean>?

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoomsList

        if (roomName != null) {
            if (other.roomName == null) return false
            if (!roomName.contentEquals(other.roomName)) return false
        } else if (other.roomName != null) return false
        if (roomIsCleaned != null) {
            if (other.roomIsCleaned == null) return false
            if (!roomIsCleaned.contentEquals(other.roomIsCleaned)) return false
        } else if (other.roomIsCleaned != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = roomName?.contentHashCode() ?: 0
        result = 31 * result + (roomIsCleaned?.contentHashCode() ?: 0)
        return result
    }
}