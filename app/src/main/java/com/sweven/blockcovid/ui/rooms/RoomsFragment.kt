package com.sweven.blockcovid.ui.rooms

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
import com.google.gson.Gson
import com.sweven.blockcovid.R
import com.sweven.blockcovid.services.APIRooms
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class RoomsFragment : Fragment() {

    private lateinit var roomsViewModel: RoomsViewModel

    private lateinit var recyclerView: RecyclerView

    private var netClient = NetworkClient()

    fun setNetwork(nc: NetworkClient) {
        netClient = nc
    }

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

        val navController: NavController = findNavController()
        val refreshButton: ExtendedFloatingActionButton = view.findViewById(R.id.refresh_button)
        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)
        loading.show()

        val retrofit = netClient.getClient()
        val service = retrofit.create(APIRooms::class.java)

        val cacheToken = File(context?.cacheDir, "token")
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }

        val getRooms = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getRooms(authorization)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        if (response.errorBody() == null) {
                            loading.hide()
                            val roomList = response.body()?.embedded?.roomWithDesksList
                            roomList?.let {
                                val listSize = roomList.size
                                val nameArray = Array(listSize) { _ -> ""}
                                val openArray = Array(listSize) { _ -> ""}
                                val closeArray = Array(listSize) { _ -> ""}
                                val daysArray = Array(listSize) { _ -> Array(7) { _ -> ""} }
                                val isOpenArray = Array(listSize) {_ -> false}
                                for (i in 0 until listSize) {
                                    nameArray[i] = roomList[i].room.name
                                    openArray[i] = roomList[i].room.openingTime.dropLast(3)
                                    closeArray[i] = roomList[i].room.closingTime.dropLast(3)

                                    for (l in roomList[i].room.openingDays.indices) {
                                        daysArray[i][l] = roomList[i].room.openingDays[l]
                                    }

                                    if (isOpen(openArray[i], closeArray[i], daysArray[i]) && !roomList[i].room.closed) {
                                        isOpenArray[i] = true
                                    }
                                }
                                recyclerView = view.findViewById(R.id.room_recycler_user)
                                val roomsAdapter = RoomsAdapter(context, navController, nameArray, openArray, closeArray, daysArray, isOpenArray)
                                recyclerView.adapter = roomsAdapter
                                recyclerView.layoutManager = LinearLayoutManager(context)
                            }
                        } else {
                            activity?.runOnUiThread {
                                loading.hide()
                                Toast.makeText(
                                        context,
                                        response.errorBody()?.string().toString(),
                                        Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    activity?.runOnUiThread {
                        loading.hide()
                        Toast.makeText(
                                context,
                                getString(R.string.error).plus(" ").plus(error.error),
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    loading.hide()
                    Toast.makeText(
                            context,
                            e.message,
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        refreshButton.setOnClickListener {
            if (!getRooms.isActive) {
                parentFragmentManager
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit()
            }
        }
    }

    private fun isOpen (ot: String, ct: String, day: Array<String>): Boolean {
        val openingTime = LocalTime.parse(ot)
        val closingTime = LocalTime.parse(ct)
        val nowTime = LocalTime.now()

        var todayOpen = false
        val thisDay = LocalDate.now().dayOfWeek.toString().toUpperCase(Locale.ITALIAN)
        for (i in day.indices) {
            if (thisDay == day[i]) {
                todayOpen = true
            }
        }
        return openingTime < nowTime && closingTime > nowTime && todayOpen
    }
}
