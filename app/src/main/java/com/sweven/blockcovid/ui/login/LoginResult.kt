package com.sweven.blockcovid.ui.login

import com.sweven.blockcovid.data.model.LoggedInUser

/**
 * Risultato dell'autenticazione: successo (dettagli utente) o messaggio di errore.
 */
data class LoginResult(
        val success: LoggedInUser? = null,
        val error: String? = null
)
