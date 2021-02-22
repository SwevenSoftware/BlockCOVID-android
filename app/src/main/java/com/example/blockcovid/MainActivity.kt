package com.example.blockcovid

import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.blockcovid.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    // NFC adapter for checking NFC state in the device
    private var nfcAdapter: NfcAdapter? = null

    // Pending intent for NFC intent foreground dispatch.
    // Used to read all NDEF tags while the app is running in the foreground.
    private var nfcPendingIntent: PendingIntent? = null
    // Optional: filter NDEF tags this app receives through the pending intent.
    //private var nfcIntentFilters: Array<IntentFilter>? = null
    //var deskList = Array(2) {Array(9) {0} }
    private var logText = "logText: "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_help, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Check if NFC is supported and enabled
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        logMessage("NFC Adapter", nfcAdapter.toString())
        logMessage("NFC supported", (nfcAdapter != null).toString())
        logMessage("NFC enabled", (nfcAdapter?.isEnabled).toString())


        // Read all tags when app is running and in the foreground
        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)


        if (intent != null) {
            // Check if the app was started via an NDEF intent
            logMessage("Found intent in onCreate", intent.action.toString())
            processIntent(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.navigation_login) {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_navigation_login)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun goScanner(view: View) {
        view.findNavController().navigate(R.id.action_navigation_home_to_navigation_scanner)
    }

    fun goPostazioni(view: View) {
        view.findNavController().navigate(R.id.action_navigation_home_to_navigation_stanza1)
    }

    fun goPrenotazioni(view: View) {
        val idPostazione = view.contentDescription.toString()
        val action = MobileNavigationDirections.actionGlobalNavigationPrenotazioni(idPostazione)
        view.findNavController().navigate(action)
    }

    fun goStanza1(view: View) {
        view.findNavController().navigate(R.id.action_global_navigation_stanza1)
    }

    fun goStanza2(view: View) {
        view.findNavController().navigate(R.id.action_global_navigation_stanza2)
    }

    fun refreshLogs(view: View) {
        println(logText)
        val tvMessages = findViewById<TextView>(R.id.tv_messages)
        val svMessages = findViewById<ScrollView>(R.id.sv_messages)
        tvMessages.text = logText
        svMessages.post {
            svMessages.smoothScrollTo(0, svMessages.bottom)
        }
    }

    fun openTimePicker(view: View) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            when (view.id) {
                R.id.editOrarioArrivo -> {
                    val orarioArrivo = findViewById<TextView>(R.id.editOrarioArrivo)
                    orarioArrivo.text = SimpleDateFormat("HH:mm", Locale.ITALIAN).format(cal.time)
                }
                R.id.editOrarioUscita -> {
                    val orarioUscita = findViewById<TextView>(R.id.editOrarioUscita)
                    orarioUscita.text = SimpleDateFormat("HH:mm", Locale.ITALIAN).format(cal.time)
                }
            }
        }
        TimePickerDialog(this, 2, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    /*fun deskColor(view: View) {
        when (view.id) {
            R.id.imageButton00 -> {
                when (deskList[0][0]) {
                    0 -> {
                        view.setBackgroundResource(R.drawable.blue_desk)
                        deskList[0][0] = 1
                    }
                    1 -> {
                        view.setBackgroundResource(R.drawable.red_desk)
                        deskList[0][0] = 2
                    }
                    2 -> {
                        view.setBackgroundResource(R.drawable.green_desk)
                        deskList[0][0] = 0
                    }
                }
            }
            R.id.imageButton01 -> {
                when (deskList[0][1]) {
                    0 -> {
                        view.setBackgroundResource(R.drawable.blue_desk)
                        deskList[0][1] = 1
                    }
                    1 -> {
                        view.setBackgroundResource(R.drawable.red_desk)
                        deskList[0][1] = 2
                    }
                    2 -> {
                        view.setBackgroundResource(R.drawable.green_desk)
                        deskList[0][1] = 0
                    }
                }
            }
            R.id.imageButton02 -> {
                when (deskList[0][2]) {
                    0 -> {
                        view.setBackgroundResource(R.drawable.blue_desk)
                        deskList[0][2] = 1
                    }
                    1 -> {
                        view.setBackgroundResource(R.drawable.red_desk)
                        deskList[0][2] = 2
                    }
                    2 -> {
                        view.setBackgroundResource(R.drawable.green_desk)
                        deskList[0][2] = 0
                    }
                }
            }
            R.id.imageButton03 -> {
                when (deskList[0][3]) {
                    0 -> {
                        view.setBackgroundResource(R.drawable.blue_desk)
                        deskList[0][3] = 1
                    }
                    1 -> {
                        view.setBackgroundResource(R.drawable.red_desk)
                        deskList[0][3] = 2
                    }
                    2 -> {
                        view.setBackgroundResource(R.drawable.green_desk)
                        deskList[0][3] = 0
                    }
                }
            }
            R.id.imageButton04 -> {
                when (deskList[0][4]) {
                    0 -> {
                        view.setBackgroundResource(R.drawable.blue_desk)
                        deskList[0][4] = 1
                    }
                    1 -> {
                        view.setBackgroundResource(R.drawable.red_desk)
                        deskList[0][4] = 2
                    }
                    2 -> {
                        view.setBackgroundResource(R.drawable.green_desk)
                        deskList[0][4] = 0
                    }
                }
            }
            R.id.imageButton05 -> {
                when (deskList[0][5]) {
                    0 -> {
                        view.setBackgroundResource(R.drawable.blue_desk)
                        deskList[0][5] = 1
                    }
                    1 -> {
                        view.setBackgroundResource(R.drawable.red_desk)
                        deskList[0][5] = 2
                    }
                    2 -> {
                        view.setBackgroundResource(R.drawable.green_desk)
                        deskList[0][5] = 0
                    }
                }
            }
            R.id.imageButton06 -> {
                when (deskList[0][6]) {
                    0 -> {
                        view.setBackgroundResource(R.drawable.blue_desk)
                        deskList[0][6] = 1
                    }
                    1 -> {
                        view.setBackgroundResource(R.drawable.red_desk)
                        deskList[0][6] = 2
                    }
                    2 -> {
                        view.setBackgroundResource(R.drawable.green_desk)
                        deskList[0][6] = 0
                    }
                }
            }
            R.id.imageButton07 -> {
                when (deskList[0][7]) {
                    0 -> {
                        view.setBackgroundResource(R.drawable.blue_desk)
                        deskList[0][7] = 1
                    }
                    1 -> {
                        view.setBackgroundResource(R.drawable.red_desk)
                        deskList[0][7] = 2
                    }
                    2 -> {
                        view.setBackgroundResource(R.drawable.green_desk)
                        deskList[0][7] = 0
                    }
                }
            }
            R.id.imageButton07 -> {
                when (deskList[0][8]) {
                    0 -> {
                        view.setBackgroundResource(R.drawable.blue_desk)
                        deskList[0][8]=1
                    }
                    1 -> {
                        view.setBackgroundResource(R.drawable.red_desk)
                        deskList[0][8]= 2
                    }
                    2 -> {
                        view.setBackgroundResource(R.drawable.green_desk)
                        deskList[0][8]= 0
                    }
                }
            }
        }
    }*/

    override fun onResume() {
        super.onResume()
        // Get all NDEF discovered intents
        // Makes sure the app gets all discovered NDEF messages as long as it's in the foreground.
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
        // Alternative: only get specific HTTP NDEF intent
        //nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, nfcIntentFilters, null);
    }

    override fun onPause() {
        super.onPause()
        // Disable foreground dispatch, as this activity is no longer in the foreground
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logMessage("Found intent in onNewIntent", intent?.action.toString())
        // If we got an intent while the app is running, also check if it's a new NDEF message
        // that was discovered
        if (intent != null) processIntent(intent)
    }

    /**
     * Check if the Intent has the action "ACTION_NDEF_DISCOVERED". If yes, handle it
     * accordingly and parse the NDEF messages.
     * @param checkIntent the intent to parse and handle if it's the right type
     */

    private fun processIntent(checkIntent: Intent) {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            logMessage("New NDEF intent", checkIntent.toString())

            // Retrieve the raw NDEF message from the tag
            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMessages != null) {
                logMessage("Raw messages", rawMessages.size.toString())
            }

            // Complete variant: parse NDEF messages
            if (rawMessages != null) {
                val messages = arrayOfNulls<NdefMessage?>(rawMessages.size)// Array<NdefMessage>(rawMessages.size, {})
                for (i in rawMessages.indices) {
                    messages[i] = rawMessages[i] as NdefMessage
                }
                // Process the messages array.
                processNdefMessages(messages)
            }

            // Simple variant: assume we have 1x URI record
            //if (rawMessages != null && rawMessages.isNotEmpty()) {
            //    val ndefMsg = rawMessages[0] as NdefMessage
            //    if (ndefMsg.records != null && ndefMsg.records.isNotEmpty()) {
            //        val ndefRecord = ndefMsg.records[0]
            //        if (ndefRecord.toUri() != null) {
            //            logMessage("URI detected", ndefRecord.toUri().toString())
            //        } else {
            //            // Other NFC Tags
            //            logMessage("Payload", ndefRecord.payload.contentToString())
            //        }
            //    }
            //}

        }
    }

    /**
     * Parse the NDEF message contents and print these to the on-screen log.
     */
    private fun processNdefMessages(ndefMessages: Array<NdefMessage?>) {
        // Go through all NDEF messages found on the NFC tag
        for (curMsg in ndefMessages) {
            if (curMsg != null) {
                // Print generic information about the NDEF message
                logMessage("Message", curMsg.toString())
                logMessage("Records", curMsg.records.size.toString())

                // Loop through all the records contained in the message
                for (curRecord in curMsg.records) {
                    if (curRecord.toUri() != null) {
                        // URI NDEF Tag
                        logMessage("- URI", curRecord.toUri().toString())
                    } else {
                        // Other NDEF Tags - simply print the payload
                        logMessage("- Contents", curRecord.payload.contentToString())
                    }
                }
            }
        }
    }

    // --------------------------------------------------------------------------------
    // Utility functions

    /**
     * Log a message to the debug text view.
     * @param header title text of the message, printed in bold
     * @param text optional parameter containing details about the message. Printed in plain text.
     */
    private fun logMessage(header: String, text: String?) {
        logText = logText.plus(if (text.isNullOrBlank()) fromHtml("<b>$header</b><br>") else fromHtml("<b>$header</b>: $text<br>"))
    }

    /**
     * Convert HTML formatted strings to spanned (styled) text, for inserting to the TextView.
     * Externalized into an own function as the fromHtml(html) method was deprecated with
     * Android N. This method chooses the right variant depending on the OS.
     * @param html HTML-formatted string to convert to a Spanned text.
     */

    private fun fromHtml(html: String): Spanned {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }
    }
}