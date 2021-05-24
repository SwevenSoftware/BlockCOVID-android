package com.sweven.blockcovid.ui.userRooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.sweven.blockcovid.R
import java.io.File

class UserRoomsFragment : Fragment() {

    private lateinit var userRoomsViewModel: UserRoomsViewModel

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userRoomsViewModel =
            ViewModelProvider(this, UserRoomsViewModelFactory()).get(UserRoomsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_user_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val refreshButton: ExtendedFloatingActionButton = view.findViewById(R.id.refresh_button)
        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)
        val mainActivity = viewLifecycleOwner

        userRoomsViewModel.userRoomsResult.observe(
            mainActivity,
            {
                createUserRoomList(it, loading)
            }
        )

        loading.show()

        val cacheToken = File(context?.cacheDir, "token")
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }

        userRoomsViewModel.showRooms(authorization)

        refreshButton.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit()
        }
    }

    fun createUserRoomList(
        userRoomsResult: UserRoomsResult,
        loading: CircularProgressIndicator
    ) {
        loading.hide()
        if (userRoomsResult.success != null) {
            val nameArray = userRoomsResult.success.roomName
            val openArray = userRoomsResult.success.roomOpen
            val closeArray = userRoomsResult.success.roomClose
            val daysArray = userRoomsResult.success.roomDays
            val isOpenArray = userRoomsResult.success.roomIsOpen

            if (nameArray != null && openArray != null && closeArray != null &&
                daysArray != null && isOpenArray != null
            ) {
                if (nameArray.isNotEmpty()) {
                    val navController: NavController = findNavController()
                    recyclerView = view?.findViewById(R.id.room_recycler_user)!!
                    val roomsAdapter = UserRoomsAdapter(context, navController, nameArray, openArray, closeArray, daysArray, isOpenArray)
                    recyclerView.adapter = roomsAdapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
                }
            } else {
                showRoomsFailed(getString(R.string.no_rooms))
            }
        } else if (userRoomsResult.error != null) {
            showRoomsFailed(userRoomsResult.error)
        }
    }

    fun showRoomsFailed(errorString: String) {
        Toast.makeText(context, getString(R.string.error).plus(" ").plus(errorString), Toast.LENGTH_SHORT).show()
    }
}
