package com.sweven.blockcovid.ui.roomView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.data.RoomViewRepository

/**
 * Factory di provider ViewModel per istanziare RoomViewViewModel.
 * Obbligatorio dato che RoomViewViewModel ha un costruttore non vuoto
 */
class RoomViewViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewViewModel::class.java)) {
            return RoomViewViewModel(
                roomViewRepository = RoomViewRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
