package com.sweven.blockcovid.data.model

data class UserRoomsList(
    val roomName: Array<String>?,
    val roomOpen: Array<String>?,
    val roomClose: Array<String>?,
    val roomDays: Array<Array<String>>?,
    val roomIsOpen: Array<Boolean>?

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserRoomsList

        if (roomName != null) {
            if (other.roomName == null) return false
            if (!roomName.contentEquals(other.roomName)) return false
        } else if (other.roomName != null) return false
        if (roomOpen != null) {
            if (other.roomOpen == null) return false
            if (!roomOpen.contentEquals(other.roomOpen)) return false
        } else if (other.roomOpen != null) return false
        if (roomClose != null) {
            if (other.roomClose == null) return false
            if (!roomClose.contentEquals(other.roomClose)) return false
        } else if (other.roomClose != null) return false
        if (roomDays != null) {
            if (other.roomDays == null) return false
            if (!roomDays.contentDeepEquals(other.roomDays)) return false
        } else if (other.roomDays != null) return false
        if (roomIsOpen != null) {
            if (other.roomIsOpen == null) return false
            if (!roomIsOpen.contentEquals(other.roomIsOpen)) return false
        } else if (other.roomIsOpen != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = roomName?.contentHashCode() ?: 0
        result = 31 * result + (roomOpen?.contentHashCode() ?: 0)
        result = 31 * result + (roomClose?.contentHashCode() ?: 0)
        result = 31 * result + (roomDays?.contentDeepHashCode() ?: 0)
        result = 31 * result + (roomIsOpen?.contentHashCode() ?: 0)
        return result
    }
}
