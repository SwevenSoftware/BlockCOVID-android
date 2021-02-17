package com.example.blockcovid.ui.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.blockcovid.R

class ScannerFragment : Fragment() {

    private lateinit var scannerViewModel: ScannerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scannerViewModel =
            ViewModelProvider(this).get(ScannerViewModel::class.java)
        return inflater.inflate(R.layout.fragment_scanner, container, false)
    }
}