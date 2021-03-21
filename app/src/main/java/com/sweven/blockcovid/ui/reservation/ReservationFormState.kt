package com.sweven.blockcovid.ui.reservation

/**
 * Stato di convalida dei dati del modulo di prenotazione
 */
data class ReservationFormState(
    val arrivalTimeError: Int? = null,
    val exitTimeError: Int? = null,
    val selectedDateError: Int? = null,
    val isDataValid: Boolean = false
)
