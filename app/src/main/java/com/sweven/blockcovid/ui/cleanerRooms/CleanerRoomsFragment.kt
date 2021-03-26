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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class CleanerRoomsFragment : Fragment() {

    private lateinit var cleanerRoomsViewModel: CleanerRoomsViewModel

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
        cleanerRoomsViewModel =
                ViewModelProvider(this).get(CleanerRoomsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_cleaner_rooms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                                val isCleanArray = Array(listSize) { _ -> true}
                                for (i in 0 until listSize) {
                                    nameArray[i] = roomList[i].room.name
                                }
                                recyclerView = view.findViewById(R.id.room_recycler_cleaner)
                                val cleanerRoomsAdapter = CleanerRoomsAdapter(context, nameArray, isCleanArray)
                                recyclerView.adapter = cleanerRoomsAdapter
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
}
