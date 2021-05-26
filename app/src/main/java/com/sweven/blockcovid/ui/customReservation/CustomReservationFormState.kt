package com.sweven.blockcovid.ui.customReservation

/**
 * Stato di convalida dei dati del modulo di prenotazione
 */

data class CustomReservationFormState(
    val arrivalTimeError: Int? = null,
    val exitTimeError: Int? = null,
    val selectedDateError: Int? = null,
    val isDataValid: Boolean = false
)
