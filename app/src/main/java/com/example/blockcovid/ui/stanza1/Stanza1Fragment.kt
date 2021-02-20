package com.example.blockcovid.ui.stanza1

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.blockcovid.R

class Stanza1Fragment : Fragment(){
    private lateinit var stanza1ViewModel: Stanza1ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stanza1ViewModel =
                ViewModelProvider(this).get(Stanza1ViewModel::class.java)
        return inflater.inflate(R.layout.fragment_stanza1, container, false)
    }
}
