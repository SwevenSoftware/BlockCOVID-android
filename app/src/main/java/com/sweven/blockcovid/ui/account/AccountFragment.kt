package com.sweven.blockcovid.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sweven.blockcovid.R
import java.io.File

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cacheToken = File(context?.cacheDir, "token")
        val cacheExpiry = File(context?.cacheDir, "expiryDate")
        val cacheUser = File(context?.cacheDir, "username")
        val cacheAuth = File(context?.cacheDir, "authority")


        // Mostra il nome dell'utente in cima alla pagina
        val showUser: TextView = view.findViewById(R.id.showUsername)
        if(cacheUser.exists()) {
            showUser.text = getString(R.string.welcome).plus(" ").plus(cacheUser.readText())
        }

        // Funzione per navigare da Account a ChangePassword (bottone Change Password)
        val changePasswordButton: Button = view.findViewById(R.id.change_password_button)
        changePasswordButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_navigation_account_to_navigation_change_password)
        }

        // Funzione per fare il logout, elimina il file token dalla cache
        val logoutButton: Button = view.findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            if(cacheToken.exists()) {
                cacheToken.delete()
                cacheExpiry.delete()
                cacheUser.delete()
                cacheAuth.delete()
                view.findNavController().navigate(R.id.action_global_navigation_login)
            }
        }
    }

}
