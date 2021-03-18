package com.sweven.blockcovid.ui.changePassword

/**
 * Stato di convalida dei dati del modulo di cambio password.
 */
data class ChangePasswordFormState(
    val oldPasswordError: Int? = null,
    val newPasswordError: Int? = null,
    val repeatPasswordError: Int? = null,
    val isDataValid: Boolean = false
)
