package com.sweven.blockcovid.ui.cleanerRooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sweven.blockcovid.R

class CleanerRoomsFragment : Fragment() {

    private lateinit var cleanerRoomsViewModel: CleanerRoomsViewModel

    private lateinit var recyclerView: RecyclerView
    private var s1 = arrayOf("Stanza 1", "Stanza 2", "Stanza 3", "Stanza 4", "Stanza 5", "Stanza 6", "Stanza 7", "Stanza 8", "Stanza 9")

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        cleanerRoomsViewModel =
                ViewModelProvider(this).get(CleanerRoomsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_cleaner_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.room_recycler_cleaner)
        val roomsAdapter = CleanerRoomsAdapter(context, s1)
        recyclerView.adapter = roomsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}
