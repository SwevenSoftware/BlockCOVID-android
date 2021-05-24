package com.sweven.blockcovid.ui.userReservations

import com.sweven.blockcovid.data.model.UserReservationsList

data class UserReservationsResult(
    val success: UserReservationsList? = null,
    val error: String? = null

)
