package com.sweven.blockcovid.ui.cleanerRooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.CleanerRoomsRepository
import com.sweven.blockcovid.data.CleanRoomRepository

/**
 * Factory di provider ViewModel per istanziare CleanerRoomsViewModel.
 * Obbligatorio dato che CleanerRoomsdViewModel ha un costruttore non vuoto
 */

class CLeanerRoomsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CleanerRoomsViewModel::class.java)) {
            return CleanerRoomsViewModel(
               cleanerRoomsRepository = CleanerRoomsRepository(),
                cleanRoomRepository = CleanRoomRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
