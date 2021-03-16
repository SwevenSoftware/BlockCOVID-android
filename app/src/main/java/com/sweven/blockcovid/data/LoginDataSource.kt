package com.sweven.blockcovid.data

import com.sweven.blockcovid.data.model.LoggedInUser

/**
 * Classe che gestisce l'autenticazione con le credenziali di accesso e recupera le informazioni sull'utente.
 */
class LoginDataSource {

    fun login(username: String, password: String, token: String, expiryDate: Long): Result<LoggedInUser> {
        try {
            val user = LoggedInUser(java.util.UUID.randomUUID().toString(), username, token, expiryDate)
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error("Error logging in")
        }
    }

    fun logout() {
    }
}
