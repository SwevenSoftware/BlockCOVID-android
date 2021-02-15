package com.example.blockcovid.ui.postazioni

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.blockcovid.R

class PostazioniFragment : Fragment(){
    private lateinit var postazioniViewModel: PostazioniViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        postazioniViewModel =
                ViewModelProvider(this).get(PostazioniViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_postazioni, container, false)
        val textView: TextView = root.findViewById(R.id.text_postazioni)
        postazioniViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
