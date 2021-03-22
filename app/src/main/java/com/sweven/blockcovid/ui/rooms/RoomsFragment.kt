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
    private var s1 = arrayOf("Stanza 1", "Stanza 2", "Stanza 3", "Stanza 4", "Stanza 5", "Stanza 6", "Stanza 7", "Stanza 8", "Stanza 9")
    private var s2 = arrayOf("14/24", "2/31", "4/13", "1/8", "2/21", "9/27", "0/15", "6/18", "3/24")

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        roomsViewModel =
                ViewModelProvider(this).get(RoomsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_user_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.room_recycler_user)
        val roomsAdapter = RoomsAdapter(context, s1, s2)
        recyclerView.adapter = roomsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}
