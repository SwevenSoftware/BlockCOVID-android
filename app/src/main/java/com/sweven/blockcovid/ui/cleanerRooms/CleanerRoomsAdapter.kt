package com.sweven.blockcovid.ui.cleanerRooms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.Gson
import com.sweven.blockcovid.R
import com.sweven.blockcovid.services.APIClean
import com.sweven.blockcovid.services.NetworkClient
import com.sweven.blockcovid.services.gsonReceive.ErrorBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class CleanerRoomsAdapter(ct: Context?, ac: FragmentActivity?, rl: Array<String>, rc: Array<Boolean>, ld: CircularProgressIndicator): RecyclerView.Adapter<CleanerRoomsAdapter.MyViewHolder>() {

    val context = ct
    val activity = ac
    private val roomList = rl
    private val roomCleaned = rc
    private val loading = ld

    private var netClient = NetworkClient()

    fun setNetwork(nc: NetworkClient) {
        netClient = nc
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var roomText: TextView = itemView.findViewById(R.id.room_text)
        var roomStatus: TextView = itemView.findViewById(R.id.room_status)
        var roomCheckBox: CheckBox = itemView.findViewById(R.id.room_checkbox)
        var roomCard: CardView = itemView.findViewById(R.id.room_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.recycler_row_cleaner, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.roomText.text = roomList[position]
        if (roomCleaned[position]) {
            holder.roomStatus.text =
                context?.getString(R.string.room_status).plus(" ").plus(context?.getString(R.string.room_clean))
            if (context != null) {
                holder.roomStatus.setTextColor(context.getColor(R.color.green_500))
            }
            holder.roomCheckBox.isChecked = true
            holder.roomCard.isClickable = false
        } else {
            holder.roomStatus.text =
                context?.getString(R.string.room_status).plus(" ").plus(context?.getString(R.string.room_to_clean))
            if (context != null) {
                holder.roomStatus.setTextColor(context.getColor(R.color.red_600))
            }
            holder.roomCheckBox.isChecked = false
            holder.roomCard.isClickable = true
        }
        holder.roomCard.setOnClickListener {
            if (!holder.roomCheckBox.isChecked) {

                loading.show()

                val retrofit = netClient.getClient()
                val service = retrofit.create(APIClean::class.java)

                val cacheToken = File(context?.cacheDir, "token")
                var authorization = ""
                if (cacheToken.exists()) {
                    authorization = cacheToken.readText()
                }

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response =
                                service.cleanRoom(authorization, holder.roomText.text.toString())
                        if (response.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                if (response.errorBody() == null) {
                                    holder.roomStatus.text =
                                            context?.getString(R.string.room_status).plus(" ")
                                                    .plus(context?.getString(R.string.room_to_clean))
                                    if (context != null) {
                                        holder.roomStatus.setTextColor(context.getColor(R.color.green_500))
                                    }
                                    holder.roomCheckBox.isChecked = true
                                    holder.roomCard.isClickable = false
                                    activity?.runOnUiThread {
                                        loading.hide()
                                        Toast.makeText(
                                                context,
                                                context?.getString(R.string.room_cleaned),
                                                Toast.LENGTH_LONG
                                        ).show()
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
                                        context?.getString(R.string.error).plus(" ").plus(error.error),
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
    }

    override fun getItemCount(): Int {
        return roomList.size
    }
}