package com.sweven.blockcovid.ui.cleanerRooms

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.sweven.blockcovid.R
import com.sweven.blockcovid.data.CleanRoomRepository
import java.io.File
import com.sweven.blockcovid.data.Result

class CleanerRoomsAdapter(ct: Context?, ac: FragmentActivity?, rl: Array<String>, rc: Array<Boolean>, ld: CircularProgressIndicator,lc:LifecycleOwner): RecyclerView.Adapter<CleanerRoomsAdapter.MyViewHolder>() {

    val context = ct
    val activity = ac
    private val roomList = rl
    private val roomCleaned = rc
    private val loading = ld
    private val lifecycle = lc
    private val cleanerRoomRepository = CleanRoomRepository()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        val typedValue = TypedValue()
        if (roomCleaned[position]) {
            holder.roomStatus.text =
                context?.getString(R.string.room_status).plus(" ")
                    .plus(context?.getString(R.string.room_clean))
            if (context != null) {
                context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
                holder.roomStatus.setTextColor(typedValue.data)
            }
            holder.roomCheckBox.isChecked = true
            holder.roomCard.isClickable = false
        } else {
            holder.roomStatus.text =
                context?.getString(R.string.room_status).plus(" ")
                    .plus(context?.getString(R.string.room_to_clean))
            if (context != null) {
                context.theme.resolveAttribute(R.attr.colorError, typedValue, true)
                holder.roomStatus.setTextColor(typedValue.data)
            }
            holder.roomCheckBox.isChecked = false
            holder.roomCard.isClickable = true
        }
        //spostare

        holder.roomCard.setOnClickListener {
            if (!holder.roomCheckBox.isChecked) {
                cleanerRoomRepository.serverResponse.observe(lifecycle, { ott ->

                    ott.getContentIfNotHandled()?.let {
                        cleanRoom(
                            it,
                            loading,
                            holder.roomStatus,
                            holder.roomCard,
                            holder.roomCheckBox
                        )
                    }
                })

                loading.show()

                val cacheToken = File(context?.cacheDir, "token")
                var authorization = ""
                if (cacheToken.exists()) {
                    authorization = cacheToken.readText()
                }
                cleanerRoomRepository.cleanRoom(authorization, holder.roomText.text.toString())
            }
        }
    }

    fun cleanRoom(
        formResult: Result<String>, loading: CircularProgressIndicator,
        roomStatus: TextView, roomCard: CardView, roomCheckBox: CheckBox
    ) {
        loading.hide()
        if (formResult is Result.Success) {
            val typedValue = TypedValue()
            roomStatus.text =
                context?.getString(R.string.room_status).plus(" ")
                    .plus(context?.getString(R.string.room_to_clean))
            if (context != null) {
                context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
                roomStatus.setTextColor(typedValue.data)
            }
            roomCheckBox.isChecked = true
            roomCard.isClickable = false
            activity?.runOnUiThread {
                loading.hide()
                Toast.makeText(
                    context,
                    context?.getString(R.string.room_cleaned),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (formResult is Result.Error) {
            showDesksFailed(formResult.exception)
        }
    }

    fun showDesksFailed(errorString: String){
        Toast.makeText(context,context?.getString(R.string.error).plus(" ").plus(errorString),Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return roomList.size
    }
}
