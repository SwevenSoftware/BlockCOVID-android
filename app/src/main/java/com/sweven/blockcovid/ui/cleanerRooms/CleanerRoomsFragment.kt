package com.sweven.blockcovid.ui.cleanerRooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.Gson
import com.sweven.blockcovid.R
import com.sweven.blockcovid.services.APIRooms
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import com.sweven.blockcovid.ui.roomView.RoomViewViewModel
import com.sweven.blockcovid.ui.roomView.RoomViewViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class CleanerRoomsFragment : Fragment() {

    private lateinit var cleanerRoomsViewModel: CleanerRoomsViewModel

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cleanerRoomsViewModel =
            ViewModelProvider(this, CLeanerRoomsViewModelFactory()).get(CleanerRoomsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_cleaner_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val refreshButton: ExtendedFloatingActionButton = view.findViewById(R.id.refresh_button)
        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)
        val mainActivity = viewLifecycleOwner

        cleanerRoomsViewModel.cleanerRoomsResult.observe(mainActivity, {
            createCleanerRoomList(it, loading)
        })
        loading.show()


        val cacheToken = File(context?.cacheDir, "token")
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }

        cleanerRoomsViewModel.showRooms(authorization)

        refreshButton.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit()
        }
    }

    fun createCleanerRoomList(
        cleanerRoomsResult: CleanerRoomsResult,
        loading: CircularProgressIndicator
    ) {

        if (cleanerRoomsResult.success != null) {
            val roomList = cleanerRoomsResult.success.roomName
            val roomCleaned = cleanerRoomsResult.success.roomIsCleaned
            if (roomList != null && roomCleaned != null) {
                if (roomList.isNotEmpty())
                    recyclerView = view?.findViewById(R.id.room_recycler_cleaner)!!
                val mainActivity = viewLifecycleOwner
                val cleanerRoomsAdapter =
                    CleanerRoomsAdapter(context, activity, roomList, roomCleaned, loading,mainActivity)
                    recyclerView.adapter = cleanerRoomsAdapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                showDesksFailed(getString(R.string.room_empty))
            }
    } else if (cleanerRoomsResult.error != null)
    {
        showDesksFailed(cleanerRoomsResult.error)
    }
    }

    fun showDesksFailed(errorString: String){
        Toast.makeText(context,getString(R.string.error).plus(" ").plus(errorString),Toast.LENGTH_SHORT).show()
    }
}
