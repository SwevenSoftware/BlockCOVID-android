package com.sweven.blockcovid.ui.home

import com.sweven.blockcovid.data.model.DeskReservationData

/**
 * Risultato del controllo della prenotazione della postazione: successo (postazione attualmente prenotata) o messaggio di errore.
 */
data class DeskReservationResult(
    val success: DeskReservationData? = null,
    val error: String? = null
)
