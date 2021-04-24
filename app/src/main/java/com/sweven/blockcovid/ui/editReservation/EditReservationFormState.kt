package com.sweven.blockcovid.ui.editReservation

/**
 * Stato di convalida dei dati del modulo di prenotazione
 */
data class EditReservationFormState(
    val arrivalTimeError: Int? = null,
    val exitTimeError: Int? = null,
    val selectedDateError: Int? = null,
    val isDataValid: Boolean = false
)
