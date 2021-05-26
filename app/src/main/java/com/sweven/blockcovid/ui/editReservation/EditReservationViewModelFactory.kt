package com.sweven.blockcovid.ui.editReservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.repositories.DeleteReservationRepository
import com.sweven.blockcovid.data.repositories.EditReservationRepository
import com.sweven.blockcovid.data.repositories.RoomViewRepository
import com.sweven.blockcovid.services.NetworkClient

/**
 * Factory di provider ViewModel per istanziare ReservationViewModel.
 * Obbligatorio dato che ReservationViewModel ha un costruttore non vuoto
 */

class EditReservationViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditReservationViewModel::class.java)) {
            return EditReservationViewModel(
                editReservationRepository = EditReservationRepository(NetworkClient()),
                deleteReservationRepository = DeleteReservationRepository(NetworkClient()),
                roomViewRepository = RoomViewRepository(NetworkClient())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
