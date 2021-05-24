package com.sweven.blockcovid.ui.settings

import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sweven.blockcovid.R
import java.io.File

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)

        val theme = File(context?.cacheDir, "theme")

        val themeSwitcher: SwitchCompat = view.findViewById(R.id.dark_theme_switch)
        val rfidChecker: Button = view.findViewById(R.id.nfc_reader_button)

        if (theme.readText() == "1") {
            themeSwitcher.isChecked = true
        }

        themeSwitcher.setOnClickListener {
            if (theme.readText() == "1") {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                theme.writeText("0")
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                theme.writeText("1")
            }
        }

        rfidChecker.setOnClickListener {
            if (nfcAdapter != null) {
                Toast.makeText(context, getString(R.string.nfc_available), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, getString(R.string.error).plus(" ").plus(getString(R.string.nfc_disabled)), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
