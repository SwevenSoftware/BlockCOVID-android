package com.sweven.blockcovid

object InputChecks {

    // Un controllo di convalida del nome utente
    fun isUsernameValid(username: String): Boolean {
        return username.isNotEmpty()
    }

    // Un controllo di convalida della password
    fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    // Un controllo se due password corrispondono
    fun isPasswordSame(password1: String, password2: String): Boolean {
        return password1 == password2
    }
}
