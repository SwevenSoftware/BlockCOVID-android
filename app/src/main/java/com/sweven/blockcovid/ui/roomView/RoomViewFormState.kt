package com.sweven.blockcovid.ui.roomView

/**
 * Stato di convalida dei dati del modulo di prenotazione
 */
data class RoomViewFormState(
    val arrivalTimeError: Int? = null,
    val exitTimeError: Int? = null,
    val selectedDateError: Int? = null,
    val isDataValid: Boolean = false
)
