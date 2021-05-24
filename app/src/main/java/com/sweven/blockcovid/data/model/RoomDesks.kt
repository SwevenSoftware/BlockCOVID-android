package com.sweven.blockcovid.data.model

data class RoomDesks(
    val openingTime: String?,
    val closingTime: String?,
    val openingDays: Array<String>?,
    val idArray: Array<String>?,
    val xArray: Array<Int>?,
    val yArray: Array<Int>?,
    val availableArray: Array<Boolean>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoomDesks

        if (openingTime != other.openingTime) return false
        if (closingTime != other.closingTime) return false
        if (openingDays != null) {
            if (other.openingDays == null) return false
            if (!openingDays.contentEquals(other.openingDays)) return false
        } else if (other.openingDays != null) return false
        if (idArray != null) {
            if (other.idArray == null) return false
            if (!idArray.contentEquals(other.idArray)) return false
        } else if (other.idArray != null) return false
        if (xArray != null) {
            if (other.xArray == null) return false
            if (!xArray.contentEquals(other.xArray)) return false
        } else if (other.xArray != null) return false
        if (yArray != null) {
            if (other.yArray == null) return false
            if (!yArray.contentEquals(other.yArray)) return false
        } else if (other.yArray != null) return false
        if (availableArray != null) {
            if (other.availableArray == null) return false
            if (!availableArray.contentEquals(other.availableArray)) return false
        } else if (other.availableArray != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = openingTime?.hashCode() ?: 0
        result = 31 * result + (closingTime?.hashCode() ?: 0)
        result = 31 * result + (openingDays?.contentHashCode() ?: 0)
        result = 31 * result + (idArray?.contentHashCode() ?: 0)
        result = 31 * result + (xArray?.contentHashCode() ?: 0)
        result = 31 * result + (yArray?.contentHashCode() ?: 0)
        result = 31 * result + (availableArray?.contentHashCode() ?: 0)
        return result
    }
}
