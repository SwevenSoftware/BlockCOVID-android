package com.example.blockcovid.ui.hubstanze

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.blockcovid.R

class HubStanzeFragment : Fragment(){
    private lateinit var hubStanzeViewModel: HubStanzeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hubStanzeViewModel =
                ViewModelProvider(this).get(HubStanzeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_hubstanze, container, false)
    }


}
