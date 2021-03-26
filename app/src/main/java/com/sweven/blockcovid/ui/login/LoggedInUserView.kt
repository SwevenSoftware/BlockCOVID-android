package com.sweven.blockcovid.ui.login

/**
 * I dettagli utente dopo l'autenticazione esposta all'interfaccia utente
 */
data class LoggedInUserView(
    val displayName: String?,
    val token: String?,
    val expiryDate: Long?,
    val authority: String?
)
