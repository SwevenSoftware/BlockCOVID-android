package com.sweven.blockcovid.ui.customReservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.repositories.ReservationRepository
import com.sweven.blockcovid.data.repositories.RoomViewRepository
import com.sweven.blockcovid.services.NetworkClient

/**
 * Factory di provider ViewModel per istanziare ReservationViewModel.
 * Obbligatorio dato che ReservationViewModel ha un costruttore non vuoto
 */

class CustomReservationViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomReservationViewModel::class.java)) {
            return CustomReservationViewModel(
                reservationRepository = ReservationRepository(NetworkClient()),
                roomViewRepository = RoomViewRepository(NetworkClient())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
