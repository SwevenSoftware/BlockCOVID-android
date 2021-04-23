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
import com.sweven.blockcovid.R
import java.io.File

class CleanerRoomsFragment : Fragment() {

    private lateinit var cleanerRoomsViewModel: CleanerRoomsViewModel

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cleanerRoomsViewModel =
            ViewModelProvider(this, CleanerRoomsViewModelFactory()).get(CleanerRoomsViewModel::class.java)
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

        cleanerRoomsViewModel.cleanRoomResult.observe(mainActivity, {
            cleanRoomMessage(it, loading)
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

    fun createCleanerRoomList(cleanerRoomsResult: CleanerRoomsResult, loading: CircularProgressIndicator) {
        loading.hide()
        if (cleanerRoomsResult.success != null) {
            val roomList = cleanerRoomsResult.success.roomName
            val roomCleaned = cleanerRoomsResult.success.roomIsCleaned
            val roomOpen = cleanerRoomsResult.success.roomIsOpen
            if (roomList != null && roomCleaned != null && roomOpen != null) {
                if (roomList.isNotEmpty()) {
                    recyclerView = view?.findViewById(R.id.room_recycler_cleaner)!!
                    val cleanerRoomsAdapter =
                        CleanerRoomsAdapter(roomList, roomCleaned, roomOpen, loading, cleanerRoomsViewModel, viewLifecycleOwner)
                    recyclerView.adapter = cleanerRoomsAdapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
                }
            } else {
                showRoomsFailed(getString(R.string.no_rooms))
            }
    } else if (cleanerRoomsResult.error != null) {
            showRoomsFailed(cleanerRoomsResult.error)
        }
    }

    fun cleanRoomMessage(formResult: CleanRoomResult, loading: CircularProgressIndicator) {
        loading.hide()
        if (formResult.success != null) {
            activity?.runOnUiThread {
                loading.hide()
                Toast.makeText(
                    context,
                    context?.getString(R.string.room_cleaned),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (formResult.error != null) {
            showRoomsFailed(formResult.error)
        }
    }

    fun showRoomsFailed(errorString: String){
        Toast.makeText(context,getString(R.string.error).plus(" ").plus(errorString),Toast.LENGTH_SHORT).show()
    }
}
