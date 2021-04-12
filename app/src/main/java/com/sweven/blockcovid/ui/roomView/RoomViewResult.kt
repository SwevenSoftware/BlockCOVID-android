package com.sweven.blockcovid.ui.roomView

/**
 * Risultato della ricerca postazioni: successo (dettagli postazioni) o messaggio di errore.
 */
data class RoomViewResult (
        val success: RoomDesks? = null,
        val error: String? = null
)
