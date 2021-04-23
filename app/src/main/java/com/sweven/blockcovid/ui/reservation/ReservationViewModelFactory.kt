package com.sweven.blockcovid.ui.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.repositories.ReservationRepository
import com.sweven.blockcovid.services.NetworkClient

/**
 * Factory di provider ViewModel per istanziare ReservationViewModel.
 * Obbligatorio dato che ReservationViewModel ha un costruttore non vuoto
 */

class ReservationViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservationViewModel::class.java)) {
            return ReservationViewModel(
                reservationRepository = ReservationRepository(NetworkClient())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
