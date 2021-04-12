package com.sweven.blockcovid.ui.cleanerRooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.CleanerRoomsRepository

/**
 * Factory di provider ViewModel per istanziare CleanerRoomsViewModel.
 * Obbligatorio dato che CleanerRoomsViewModel ha un costruttore non vuoto
 */

class CleanerRoomsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CleanerRoomsViewModel::class.java)) {
            return CleanerRoomsViewModel(
                    cleanerRoomsRepository = CleanerRoomsRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
