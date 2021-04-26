package com.sweven.blockcovid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController

class CleanerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleaner)
    }

    // Funzione per far apparire il pulsante Profilo in alto a destra
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_nav_menu, menu)
        return true
    }

    // Funzione per linkare la pagina di Account tramite il pulsante in alto a destra dello schermo
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.navigation_login) {
            findNavController(R.id.nav_host_fragment_cleaner).navigate(R.id.action_global_navigation_cleaner_account)
        }
        return super.onOptionsItemSelected(item)
    }
}