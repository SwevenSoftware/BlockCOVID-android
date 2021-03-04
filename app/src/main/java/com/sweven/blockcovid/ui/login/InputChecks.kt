package com.sweven.blockcovid.ui.login

import android.util.Patterns

object InputChecks {

    // Un controllo di convalida del nome utente
    fun isUsernameValid(username: String): Boolean {
        return username.isNotEmpty()
    }

    // Un controllo di convalida della password
    fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}