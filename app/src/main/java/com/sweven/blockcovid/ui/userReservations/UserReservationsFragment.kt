package com.sweven.blockcovid.ui.userReservations

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
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.sweven.blockcovid.R
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId

class UserReservationsFragment : Fragment() {

    private lateinit var userReservationsViewModel: UserReservationsViewModel

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        userReservationsViewModel =
                ViewModelProvider(this, UserReservationsViewModelFactory()).get(UserReservationsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_user_reservations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)
        val mainActivity = viewLifecycleOwner

        userReservationsViewModel.userReservationsResult.observe(mainActivity, {
            createUserReservationsList(it, loading)
        })

        loading.show()

        val cacheToken = File(context?.cacheDir, "token")
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }
        val from = LocalDateTime.now(ZoneId.of("Europe/Rome")).toString().dropLast(7)

        userReservationsViewModel.showReservations(authorization, from)
    }

    fun createUserReservationsList(
            userReservationsResult: UserReservationsResult,
            loading: CircularProgressIndicator
    ) {
        loading.hide()
        if (userReservationsResult.success != null) {
            val reservationIdArray = userReservationsResult.success.reservationId
            val deskIdArray = userReservationsResult.success.deskId
            val startTimeArray = userReservationsResult.success.startTime
            val endTimeArray = userReservationsResult.success.endTime
            val dayArray = userReservationsResult.success.day

            if (reservationIdArray != null && deskIdArray != null && startTimeArray != null &&
                endTimeArray != null && dayArray != null) {
                if (reservationIdArray.isNotEmpty()) {
                    val navController: NavController = findNavController()
                    recyclerView = view?.findViewById(R.id.reservation_recycler_user)!!
                    val reservationsAdapter = UserReservationsAdapter(context, navController, reservationIdArray, deskIdArray, startTimeArray, endTimeArray, dayArray)
                    recyclerView.adapter = reservationsAdapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
                }
            } else {
                showReservationsFailed(getString(R.string.no_reservations))
            }
        } else if (userReservationsResult.error != null)
        {
            showReservationsFailed(userReservationsResult.error)
        }
    }

    fun showReservationsFailed(errorString: String){
        Toast.makeText(context,getString(R.string.error).plus(" ").plus(errorString),Toast.LENGTH_SHORT).show()
    }
}
