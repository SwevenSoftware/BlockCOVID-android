package com.sweven.blockcovid.data.model

/**
 * Classe di dati che cattura le informazioni sugli utenti per gli utenti
 * che hanno effettuato l'accesso recuperate da LoginRepository
 */
data class LoggedInUser(
        val displayName: String,
        val token: String?,
        val expiryDate: Long?,
        val authority: String?
)
