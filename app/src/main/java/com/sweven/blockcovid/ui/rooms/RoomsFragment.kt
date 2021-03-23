package com.sweven.blockcovid.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import java.net.SocketTimeoutException
import java.time.LocalTime

class RoomsFragment : Fragment() {

    private lateinit var roomsViewModel: RoomsViewModel

    private lateinit var recyclerView: RecyclerView

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

        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)
        loading.show()

        val retrofit = NetworkClient.retrofitClient
        val service = retrofit.create(APIRooms::class.java)

        val cacheToken = File(context?.cacheDir, "token")
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getRooms(authorization)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        if (response.errorBody() == null) {
                            loading.hide()
                            val roomList = response.body()?.embedded?.roomList
                            println(roomList)
                            println(roomList?.indices)
                            roomList?.let {
                                val listSize = roomList.size-1
                                val s1 = Array(listSize) { _ -> ""}
                                val s2 = Array(listSize) { _ -> ""}
                                val s3 = Array(listSize) { _ -> ""}
                                val s4 = Array(listSize) {_ -> false}
                                for (i in 0 until listSize) {
                                    s1[i] = roomList[i].name
                                    s2[i] = roomList[i].openingTime.dropLast(3)
                                    s3[i] = roomList[i].closingTime.dropLast(3)
                                    if (isOpen(s2[i], s3[i])) {
                                        s4[i] = true
                                    }
                                }
                                recyclerView = view.findViewById(R.id.room_recycler_user)
                                val roomsAdapter = RoomsAdapter(context, s1, s2, s3, s4)
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
                    when (error.status.toString()) {
                        "400" ->
                            activity?.runOnUiThread {
                                loading.hide()
                                Toast.makeText(
                                        context,
                                        getString(R.string.old_password_incorrect),
                                        Toast.LENGTH_LONG
                                ).show()
                            }
                        "401" ->
                            activity?.runOnUiThread {
                                loading.hide()
                                Toast.makeText(
                                        context,
                                        getString(R.string.old_password_incorrect),
                                        Toast.LENGTH_LONG
                                ).show()
                            }
                        else ->
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
            } catch (exception: SocketTimeoutException) {
                activity?.runOnUiThread {
                    loading.hide()
                    Toast.makeText(
                            context,
                            getString(R.string.timeout),
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun isOpen (ot: String, ct: String): Boolean {
        val openingTime = LocalTime.parse(ot)
        val closingTime = LocalTime.parse(ct)
        val nowTime = LocalTime.now()
        return openingTime < nowTime && closingTime > nowTime
    }
}
