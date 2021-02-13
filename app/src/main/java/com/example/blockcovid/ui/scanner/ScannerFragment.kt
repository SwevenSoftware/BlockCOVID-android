package com.example.blockcovid.ui.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.blockcovid.R
import com.example.blockcovid.databinding.FragmentSettingsBinding
import com.example.blockcovid.ui.settings.SettingsViewModel

class ScannerFragment : Fragment(R.layout.fragment_settings){
    private lateinit var scannerViewModel: SettingsViewModel
 private lateinit var  binding: FragmentSettingsBinding
  override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
   ): View? {
       scannerViewModel =
                ViewModelProvider(this).get(SettingsViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_settings, container, false)
      binding=FragmentSettingsBinding.inflate(inflater,container,false)
//        val textView: TextView = root.findViewById(R.id.text_scanner)

        scannerViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textSettings.text = it
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnScanner.setOnClickListener {
            Toast.makeText(requireContext(),"buttonclick",Toast.LENGTH_SHORT)
                .show()
        }
    }

}
