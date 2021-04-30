package com.sweven.blockcovid.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sweven.blockcovid.R
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val args: HomeFragmentArgs by navArgs()
    private lateinit var authorization: String
    private lateinit var helperText: TextView
    private lateinit var availableTextLayout: TextInputLayout
    private lateinit var freeUntilTextLayout: TextInputLayout
    private lateinit var cleanStatusTextLayout: TextInputLayout
    private lateinit var availableTextView: TextInputEditText
    private lateinit var freeUntilTextView: TextInputEditText
    private lateinit var cleanStatusTextView: TextInputEditText
    private lateinit var cleanedYourselfLayout: FrameLayout
    private lateinit var cleanedYourselfCheckBox: CheckBox
    private lateinit var loading: CircularProgressIndicator
    private lateinit var startEndButton: Button
    private lateinit var reserveButton: Button
    private var localDeskId = ""
    private var reservationId = ""
    private var deskX: Long = 0
    private var deskY: Long = 0
    private var roomName = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory()).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.deskStatusResult.observe(viewLifecycleOwner, {
            showDeskStatusResult(it)
        })

        homeViewModel.deskInfoResult.observe(viewLifecycleOwner, {
            showDeskInfoResult(it)
        })

        homeViewModel.deskReservationResult.observe(viewLifecycleOwner, {
            showDeskReservationResult(it)
        })

        homeViewModel.startReservationResult.observe(viewLifecycleOwner, {
            showStartReservationResult(it)
        })

        homeViewModel.endReservationResult.observe(viewLifecycleOwner, {
            showEndReservationResult(it)
        })

        homeViewModel.deleteReservationResult.observe(viewLifecycleOwner, {
            showDeleteReservationResult(it)
        })

        helperText = view.findViewById(R.id.helper_text)
        availableTextLayout = view.findViewById(R.id.available_text)
        freeUntilTextLayout = view.findViewById(R.id.free_until_text)
        cleanStatusTextLayout = view.findViewById(R.id.clean_status_text)
        availableTextView = view.findViewById(R.id.edit_available_text)
        freeUntilTextView = view.findViewById(R.id.edit_free_until_text)
        cleanStatusTextView = view.findViewById(R.id.edit_clean_status_text)
        cleanedYourselfLayout = view.findViewById(R.id.frame_layout_clean)
        cleanedYourselfCheckBox = view.findViewById(R.id.cleaned_yourself_checkbox)
        startEndButton = view.findViewById(R.id.start_end_button)
        reserveButton = view.findViewById(R.id.reserve_button)
        loading = view.findViewById(R.id.loading)

        localDeskId = args.rfidContent
        args.rfidContent.drop(args.rfidContent.length)

        val cacheToken = File(context?.cacheDir, "token")
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }
        val timestamp = LocalDateTime.now(UTC).truncatedTo(ChronoUnit.MINUTES).toString()
        if (localDeskId != "") {
            loading.show()
            homeViewModel.deskStatus(authorization, timestamp, localDeskId)
            homeViewModel.deskInfo(authorization, localDeskId)
        }
    }

    fun showDeskStatusResult(formResult: DeskStatusResult) {
        loading.hide()
        if (formResult.success != null) {
            if (formResult.success.available == true) {
                availableTextView.setText(getString(R.string.desk_available))
                if (formResult.success.nextChange != null) {
                    freeUntilTextLayout.hint = getString(R.string.next_reservation)
                    freeUntilTextView.setText(formResult.success.nextChange)
                } else {
                    freeUntilTextLayout.hint = getString(R.string.next_reservation)
                    freeUntilTextView.setText(getString(R.string.none))
                }
            } else {
                availableTextView.setText(getString(R.string.desk_unavailable))
                if (formResult.success.nextChange != null) {
                    freeUntilTextLayout.hint = getString(R.string.reservation_ends)
                    freeUntilTextView.setText(formResult.success.nextChange)
                } else {
                    freeUntilTextLayout.hint = getString(R.string.reservation_ends)
                    freeUntilTextView.setText(getString(R.string.none))
                }
            }
        }
        else if (formResult.error != null) {
            showError(formResult.error)
        }
    }

    fun showDeskInfoResult(formResult: DeskInfoResult) {
        loading.hide()
        if (formResult.success != null) {
            if (formResult.success.x != null && formResult.success.y != null && formResult.success.room != null) {
                deskX = formResult.success.x
                deskY = formResult.success.y
                roomName = formResult.success.room
                if (formResult.success.deskClean == "CLEAN") {
                    cleanStatusTextView.setText(getString(R.string.desk_clean))
                } else {
                    cleanStatusTextView.setText(getString(R.string.desk_dirty))
                }
                val timestamp =
                    LocalDateTime.now(UTC).truncatedTo(ChronoUnit.MINUTES).toString()
                loading.show()
                homeViewModel.deskReservation(authorization, localDeskId, timestamp)
            }
        }
        else if (formResult.error != null) {
            showError(formResult.error)
        }
    }

    fun showDeskReservationResult (formResult: DeskReservationResult) {
        loading.hide()
        if (formResult.success != null) {
            reserveButton.setOnClickListener {
                val action = HomeFragmentDirections.actionNavigationHomeToNavigationCustomReservation(
                    localDeskId, roomName, deskX.toString(), deskY.toString()
                )
                findNavController().navigate(action)
            }
            if (formResult.success.start != null && formResult.success.id != null) {
                reservationId = formResult.success.id
                val timestamp = ZonedDateTime.now(TimeZone.getDefault().toZoneId())
                    .truncatedTo(ChronoUnit.MINUTES)
                val startTime = ZonedDateTime.parse(formResult.success.start)
                // Not started and inside reservation -> sit down and start
                if (formResult.success.usageStart == null && timestamp >= startTime) {
                    availableTextView.setText(getString(R.string.desk_reserved))
                    startEndButton.setOnClickListener {
                        loading.show()
                        homeViewModel.startReservation(reservationId, authorization)
                    }
                    startEndButton.text = getString(R.string.start)
                    startEndButton.visibility = VISIBLE
                }
                // Started and inside reservation -> get up and end
                else if (formResult.success.usageStart != null && timestamp >= ZonedDateTime.parse(
                        formResult.success.usageStart
                    )
                ) {
                    availableTextView.setText(getString(R.string.desk_reserved))
                    startEndButton.setOnClickListener {
                        loading.show()
                        homeViewModel.endReservation(formResult.success.id, authorization, cleanedYourselfCheckBox.isChecked)
                    }
                    cleanedYourselfLayout.visibility = VISIBLE
                    startEndButton.text = getString(R.string.end)
                    startEndButton.visibility = VISIBLE
                }
            }
            helperText.visibility = INVISIBLE
            availableTextLayout.visibility = VISIBLE
            cleanStatusTextLayout.visibility = VISIBLE
            reserveButton.visibility = VISIBLE
            freeUntilTextLayout.visibility = VISIBLE
        }
        else if (formResult.error != null) {
            showError(formResult.error)
        }
    }

    fun showStartReservationResult(formResult: StartReservationResult) {
        loading.hide()
        if (formResult.success != null) {
            Toast.makeText(context, getString(R.string.reservation_started),Toast.LENGTH_SHORT).show()
            // TODO add notifications every 1 hour and/or at the reservation's end
            findNavController().navigate(R.id.action_global_navigation_home)
        }
        else if (formResult.error != null) {
            showError(formResult.error)
        }
    }

    fun showEndReservationResult(formResult: EndReservationResult) {
        loading.hide()
        if (formResult.success != null) {
            Toast.makeText(context, getString(R.string.reservation_ended),Toast.LENGTH_SHORT).show()
            // TODO remove future notifications for this reservation (?)
            loading.show()
            homeViewModel.deleteReservation(reservationId, authorization)
            findNavController().navigate(R.id.action_global_navigation_home)
        }
        else if (formResult.error != null) {
            showError(formResult.error)
        }
    }

    fun showDeleteReservationResult(formResult: DeleteReservationResult) {
        loading.hide()
        if (formResult.error != null) {
            showError(formResult.error)
        }
    }

    fun showError(errorString: String) {
        Toast.makeText(context,getString(R.string.error).plus(" ").plus(errorString),Toast.LENGTH_SHORT).show()
    }
}
