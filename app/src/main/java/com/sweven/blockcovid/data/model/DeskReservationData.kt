package com.sweven.blockcovid.data.model

data class DeskReservationData (
    val id: String?,
    val room: String?,
    val start: String?,
    val end: String?,
    val usageStart: String?,
    val usageEnd: String?,
    val clean: Boolean?
)