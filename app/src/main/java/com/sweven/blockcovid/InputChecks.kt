package com.sweven.blockcovid

object InputChecks {

    // Un controllo di convalida del nome utente
    fun isFieldNotEmpty(text: String): Boolean {
        return text.isNotEmpty()
    }

    fun isUsernameValid(username: String): Boolean {
        val pattern = "^[A-Za-z0-9]{4,16}$".toRegex()
        return pattern.matches(username)
    }

    // Un controllo di convalida della password
    fun isPasswordValid(password: String): Boolean {
        return password.length > 7
    }

    // Un controllo se due password corrispondono
    fun isPasswordSame(password1: String, password2: String): Boolean {
        return password1 == password2
    }
}
