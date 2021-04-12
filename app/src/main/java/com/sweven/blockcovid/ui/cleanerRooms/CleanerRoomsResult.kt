package com.sweven.blockcovid.ui.cleanerRooms

import com.sweven.blockcovid.services.gsonReceive.Rooms

data class CleanerRoomsResult(
    val success: RoomsList? = null,
    val error: String? = null

)
