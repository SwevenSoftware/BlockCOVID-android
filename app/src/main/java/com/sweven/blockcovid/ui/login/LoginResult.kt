package com.sweven.blockcovid.ui.login

/**
 * Risultato dell'autenticazione: successo (dettagli utente) o messaggio di errore.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: String? = null
)
