package com.example.blockcovid.ui.stanza2

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.blockcovid.R

    class Stanza2Fragment : Fragment(){
        private lateinit var stanza2ViewModel: Stanza2ViewModel

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            stanza2ViewModel =
                ViewModelProvider(this).get(Stanza2ViewModel::class.java)
            return inflater.inflate(R.layout.fragment_stanza2, container, false)
        }


    }
