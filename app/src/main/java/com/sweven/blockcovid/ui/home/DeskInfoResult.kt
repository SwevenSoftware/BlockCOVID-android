package com.sweven.blockcovid.ui.home

import com.sweven.blockcovid.data.model.ThisDeskInfo

/**
 * Risultato delle info della postazione: successo (dettagli postazione) o messaggio di errore.
 */
data class DeskInfoResult(
    val success: ThisDeskInfo? = null,
    val error: String? = null
)
