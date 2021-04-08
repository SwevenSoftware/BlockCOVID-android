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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.Gson
import com.sweven.blockcovid.R
import com.sweven.blockcovid.services.APIDesks
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class RoomViewFragment : Fragment(){

    private lateinit var roomViewViewModel: RoomViewViewModel
    private val args: RoomViewFragmentArgs by navArgs()

    private var netClient = NetworkClient()

    fun setNetwork(nc: NetworkClient) {
        netClient = nc
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        roomViewViewModel =
                ViewModelProvider(this).get(RoomViewViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_room_view, container, false)
        val activity: AppCompatActivity = activity as AppCompatActivity
        val actionBar = activity.supportActionBar
        actionBar?.title = args.roomName
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController: NavController = findNavController()
        val loading: CircularProgressIndicator = view.findViewById(R.id.loading)
        loading.show()

        val retrofit = netClient.getClient()
        val service = retrofit.create(APIDesks::class.java)

        val cacheToken = File(context?.cacheDir, "token")
        val cacheTheme = File(context?.cacheDir, "theme")
        val darkTheme = cacheTheme.readText() == "1"
        var authorization = ""
        if (cacheToken.exists()) {
            authorization = cacheToken.readText()
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getDesks(authorization, args.roomName)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        if (response.errorBody() == null) {
                            loading.hide()
                            val desksList = response.body()?.desks
                            println(desksList.toString())
                            desksList?.let {
                                val listSize = desksList.size
                                val idArray = Array(listSize) { _ -> 0}
                                val xArray = Array(listSize) { _ -> 0}
                                val yArray = Array(listSize) { _ -> 0}

                                val layout: ConstraintLayout = view.findViewById(R.id.desk_layout)
                                for (i in idArray.indices) {
                                    idArray[i] = i+1
                                    xArray[i] = desksList[i].x-1
                                    yArray[i] = desksList[i].y-1

                                    val imgButton = Button(context)
                                    imgButton.id = idArray[i]
                                    imgButton.width = 50
                                    imgButton.height = 50
                                    if (darkTheme) {
                                        imgButton.background = ResourcesCompat.getDrawable(resources, R.drawable.green_desk_night, null)
                                    } else {
                                        imgButton.background = ResourcesCompat.getDrawable(resources, R.drawable.green_desk, null)
                                    }
                                    imgButton.setOnClickListener {
                                        val action = RoomViewFragmentDirections.actionNavigationRoomViewToNavigationReservation(desksList[i].x.toString(), desksList[i].y.toString(), args.roomName)
                                        navController.navigate(action)
                                    }
                                    layout.addView(imgButton)
                                    val params = imgButton.layoutParams as ConstraintLayout.LayoutParams
                                    params.leftToLeft = ConstraintSet.PARENT_ID
                                    params.leftMargin = xArray[i]*500
                                    params.topToTop = ConstraintSet.PARENT_ID
                                    params.topMargin = yArray[i]*500
                                    imgButton.layoutParams = params
                                }
                            }
                        } else {
                            activity?.runOnUiThread {
                                loading.hide()
                                Toast.makeText(
                                        context,
                                        response.errorBody()?.string().toString(),
                                        Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
                    activity?.runOnUiThread {
                        loading.hide()
                        Toast.makeText(
                                context,
                                getString(R.string.error).plus(" ").plus(error.error),
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    loading.hide()
                    Toast.makeText(
                            context,
                            e.message,
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
