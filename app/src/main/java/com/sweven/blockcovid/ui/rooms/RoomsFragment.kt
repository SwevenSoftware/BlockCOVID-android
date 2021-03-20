package com.sweven.blockcovid.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sweven.blockcovid.R

class RoomsFragment : Fragment() {

    private lateinit var roomsViewModel: RoomsViewModel

    private lateinit var recyclerView: RecyclerView
    private var s1 = arrayOf("Stanza 1", "Stanza 2")

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        roomsViewModel =
                ViewModelProvider(this).get(RoomsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.room_recycler)
        val roomsAdapter = RoomsAdapter(context, s1)
        recyclerView.adapter = roomsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}
