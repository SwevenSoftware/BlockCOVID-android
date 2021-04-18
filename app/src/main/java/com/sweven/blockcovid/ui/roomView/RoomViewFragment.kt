package com.sweven.blockcovid.ui.roomView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.sweven.blockcovid.R
import java.io.File


class RoomViewFragment : Fragment(){

    private lateinit var roomViewViewModel: RoomViewViewModel
    private val args: RoomViewFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        roomViewViewModel =
                ViewModelProvider(this, RoomViewViewModelFactory()).get(RoomViewViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_room_view, container, false)
        val activity: AppCompatActivity = activity as AppCompatActivity
        val actionBar = activity.supportActionBar
        actionBar?.title = args.roomName
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)
        val cacheTheme = File(context?.cacheDir, "theme")
        val darkTheme = cacheTheme.readText() == "1"
        val mainActivity = viewLifecycleOwner

        roomViewViewModel.roomViewResult.observe(mainActivity, {
            createRoomViewResult(it, loading, darkTheme)
        })

        loading.show()
        val cacheToken = File(context?.cacheDir, "token")
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }
        roomViewViewModel.showRooms(authorization, args.roomName)
    }

    fun createRoomViewResult(roomDesks: RoomViewResult, loading: CircularProgressIndicator, theme: Boolean) {
        loading.hide()
        if (roomDesks.success != null) {
            val idArray = roomDesks.success.idArray
            val xArray = roomDesks.success.xArray
            val yArray = roomDesks.success.yArray
            if (idArray != null && xArray != null && yArray != null) {
                if (idArray.isNotEmpty()) {
                    for (i in idArray.indices) {
                        val imgButton = Button(context)
                        imgButton.id = idArray[i]
                        imgButton.width = 50
                        imgButton.height = 50
                        if (theme) {
                            imgButton.background = ResourcesCompat.getDrawable(resources, R.drawable.green_desk_night, null)
                        } else {
                            imgButton.background = ResourcesCompat.getDrawable(resources, R.drawable.green_desk, null)
                        }
                        imgButton.setOnClickListener {
                            val action = RoomViewFragmentDirections.actionNavigationRoomViewToNavigationReservation((xArray[i] + 1).toString(), (yArray[i] + 1).toString(), args.roomName)
                            findNavController().navigate(action)
                        }
                        val layout: ConstraintLayout? = view?.findViewById(R.id.desk_layout)
                        layout?.addView(imgButton)
                        val params = imgButton.layoutParams as ConstraintLayout.LayoutParams
                        params.leftToLeft = ConstraintSet.PARENT_ID
                        params.leftMargin = xArray[i] * 500
                        params.topToTop = ConstraintSet.PARENT_ID
                        params.topMargin = yArray[i] * 500
                        imgButton.layoutParams = params
                    }
                } else {
                    showDesksFailed(getString(R.string.room_empty))
                }
            }
        } else if (roomDesks.error != null) {
            showDesksFailed(roomDesks.error)
        }
    }

    fun showDesksFailed(errorString: String){
        Toast.makeText(context,getString(R.string.error).plus(" ").plus(errorString),Toast.LENGTH_SHORT).show()
    }
}

