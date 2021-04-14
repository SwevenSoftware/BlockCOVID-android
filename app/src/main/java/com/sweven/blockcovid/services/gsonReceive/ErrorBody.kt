package com.sweven.blockcovid.services.gsonReceive

data class ErrorBody(
    val error: String,
    val message: String,
    val path: String,
    val status: Int,
    val timestamp: String
)