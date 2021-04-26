package com.sweven.blockcovid.ui.home

import com.sweven.blockcovid.data.model.DeskStatus

/**
 * Risultato dello stato della postazione: successo (stato postazione) o messaggio di errore.
 */
data class DeskStatusResult (
        val success: DeskStatus? = null,
        val error: String? = null
)
