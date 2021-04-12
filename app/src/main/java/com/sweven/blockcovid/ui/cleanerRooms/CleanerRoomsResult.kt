package com.sweven.blockcovid.ui.cleanerRooms

import com.sweven.blockcovid.data.model.CleanerRoomsList

data class CleanerRoomsResult(
        val success: CleanerRoomsList? = null,
        val error: String? = null

)
