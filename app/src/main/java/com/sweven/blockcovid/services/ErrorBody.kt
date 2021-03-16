package com.sweven.blockcovid.services

data class ErrorBody(
    val error: String,
    val message: String,
    val path: String,
    val status: Int,
    val timestamp: String
)