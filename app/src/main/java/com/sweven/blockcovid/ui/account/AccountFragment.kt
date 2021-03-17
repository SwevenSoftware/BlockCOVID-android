package com.sweven.blockcovid.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        val showUser: TextView = view.findViewById(R.id.showUsername)
        val cacheUser = File(context?.cacheDir, "username")
        if(cacheUser.exists()) {
            showUser.text = getString(R.string.welcome).plus(" ").plus(cacheUser.readText())
        }
    }
}
