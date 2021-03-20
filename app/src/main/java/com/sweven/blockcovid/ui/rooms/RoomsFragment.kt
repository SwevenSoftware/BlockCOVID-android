package com.sweven.blockcovid.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.R

class RoomsFragment : Fragment() {

    private lateinit var roomsViewModel: RoomsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        roomsViewModel =
                ViewModelProvider(this).get(RoomsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_rooms, container, false)
    }
}
