package com.sweven.blockcovid.ui.login

import android.util.Patterns

object InputChecks {

    // Un controllo di convalida del nome utente
    fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // Un controllo di convalida della password
    fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}