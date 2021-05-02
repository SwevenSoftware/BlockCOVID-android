package com.sweven.blockcovid.services.gsonReceive

data class DeskStatusLinks (
    val available: Boolean,
    val nextChange: String?,
    val links: Link
)
