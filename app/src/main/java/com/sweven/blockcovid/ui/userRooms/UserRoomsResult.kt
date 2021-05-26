package com.sweven.blockcovid.ui.userRooms

import com.sweven.blockcovid.data.model.UserRoomsList

data class UserRoomsResult(
    val success: UserRoomsList? = null,
    val error: String? = null

)
