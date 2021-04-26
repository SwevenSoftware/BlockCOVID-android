package com.sweven.blockcovid.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sweven.blockcovid.R
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.temporal.ChronoUnit

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val args: HomeFragmentArgs by navArgs()
    private lateinit var helperText: TextView
    private lateinit var availableTextLayout: TextInputLayout
    private lateinit var freeUntilTextLayout: TextInputLayout
    private lateinit var availableTextView: TextInputEditText
    private lateinit var freeUntilTextView: TextInputEditText
    private lateinit var loading: CircularProgressIndicator
    private lateinit var reserve: Button
    private var localDeskId = ""

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

        helperText = view.findViewById(R.id.helper_text)
        availableTextLayout = view.findViewById(R.id.available_text)
        freeUntilTextLayout = view.findViewById(R.id.free_until_text)
        availableTextView = view.findViewById(R.id.edit_available_text)
        freeUntilTextView = view.findViewById(R.id.edit_free_until_text)
        reserve = view.findViewById(R.id.reserve_button)

        loading = view.findViewById(R.id.loading)

        localDeskId = args.rfidContent
        args.rfidContent.drop(args.rfidContent.length)

        val cacheToken = File(context?.cacheDir, "token")
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }
        val timestamp = LocalDateTime.now(UTC).truncatedTo(ChronoUnit.MINUTES).toString()
        if (localDeskId != "") {
            loading.show()
            homeViewModel.deskStatus(authorization, timestamp, localDeskId)
        }
    }

    fun showDeskStatusResult(formResult: DeskStatusResult) {
        loading.hide()
        if (formResult.success != null) {
            helperText.visibility = INVISIBLE
            availableTextLayout.visibility = VISIBLE
            reserve.visibility = VISIBLE
            if (formResult.success.available == true) {
                availableTextView.setText(getString(R.string.desk_available))
            } else {
                availableTextView.setText(getString(R.string.desk_unavailable))
            }

            if (formResult.success.nextChange != null) {
                freeUntilTextLayout.visibility = VISIBLE
                freeUntilTextView.setText(formResult.success.nextChange)
            }

            reserve.setOnClickListener {
                // TODO: add navitation to new reservation page passing localDeskId as argument
            }
            localDeskId = ""
        }
        else if (formResult.error != null) {
            showDeskStatusFailed(formResult.error)
        }
    }

    fun showDeskStatusFailed(errorString: String) {
        Toast.makeText(context,getString(R.string.error).plus(" ").plus(errorString),Toast.LENGTH_SHORT).show()
    }
}
