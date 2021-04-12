package com.sweven.blockcovid.ui.roomView

data class RoomDesks(
        val idArray: Array<Int>?,
        val xArray: Array<Int>?,
        val yArray: Array<Int>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoomDesks

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

        return true
    }

    override fun hashCode(): Int {
        var result = idArray?.contentHashCode() ?: 0
        result = 31 * result + (xArray?.contentHashCode() ?: 0)
        result = 31 * result + (yArray?.contentHashCode() ?: 0)
        return result
    }
}