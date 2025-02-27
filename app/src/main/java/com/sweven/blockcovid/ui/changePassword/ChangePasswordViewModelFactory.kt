package com.sweven.blockcovid.ui.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.repositories.ChangePasswordRepository
import com.sweven.blockcovid.services.NetworkClient
/**
 * Factory di provider ViewModel per istanziare ChangePasswordViewModel.
 * Obbligatorio dato che ChangePasswordViewModel ha un costruttore non vuoto
 */

class ChangePasswordViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            return ChangePasswordViewModel(
                changePasswordRepository = ChangePasswordRepository(NetworkClient())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
