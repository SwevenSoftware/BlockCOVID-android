package com.example.blockcovid.ui.postazioni2

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.blockcovid.R

    class Postazioni2Fragment : Fragment(){
        private lateinit var postazioni2ViewModel: Postazioni2ViewModel

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            postazioni2ViewModel =
                ViewModelProvider(this).get(Postazioni2ViewModel::class.java)
            return inflater.inflate(R.layout.fragment_postazioni2, container, false)
        }


    }
