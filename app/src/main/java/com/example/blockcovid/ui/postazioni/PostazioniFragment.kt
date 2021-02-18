package com.example.blockcovid.ui.postazioni

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
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
        return inflater.inflate(R.layout.fragment_postazioni, container, false)
    }


}
