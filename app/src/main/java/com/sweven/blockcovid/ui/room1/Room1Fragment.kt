package com.sweven.blockcovid.ui.room1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.R

class Room1Fragment : Fragment(){
    private lateinit var room1ViewModel: Room1ViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        room1ViewModel =
                ViewModelProvider(this).get(Room1ViewModel::class.java)
        return inflater.inflate(R.layout.fragment_room1, container, false)
    }
}
