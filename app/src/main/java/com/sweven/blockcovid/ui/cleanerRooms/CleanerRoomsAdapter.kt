package com.sweven.blockcovid.ui.cleanerRooms

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.sweven.blockcovid.R
import java.io.File

class CleanerRoomsAdapter(rl: Array<String>, rc: Array<Boolean>, ro: Array<Boolean>, lo: CircularProgressIndicator, vm: CleanerRoomsViewModel, lc: LifecycleOwner) : RecyclerView.Adapter<CleanerRoomsAdapter.MyViewHolder>() {

    private val roomList = rl
    private val roomCleaned = rc
    private val roomOpened = ro
    private val loading = lo
    private val viewModel = vm
    private val lifecycleOwner = lc
    private lateinit var context: Context

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var roomText: TextView = itemView.findViewById(R.id.room_text)
        var roomStatus: TextView = itemView.findViewById(R.id.room_status)
        var roomOpen: TextView = itemView.findViewById(R.id.room_open)
        var roomCheckBox: CheckBox = itemView.findViewById(R.id.room_checkbox)
        var roomCard: CardView = itemView.findViewById(R.id.room_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleanerRoomsAdapter.MyViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.recycler_row_cleaner, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CleanerRoomsAdapter.MyViewHolder, position: Int) {

        viewModel.cleanRoomResult.observe(
            lifecycleOwner,
            {
                cleanRoom(it, holder.roomStatus, holder.roomCard, holder.roomCheckBox)
            }
        )

        holder.roomText.text = roomList[position]
        val typedValue = TypedValue()
        if (roomCleaned[position]) {
            holder.roomStatus.text =
                context.getString(R.string.room_status).plus(" ")
                    .plus(context.getString(R.string.room_clean))
            context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
            holder.roomStatus.setTextColor(typedValue.data)
            holder.roomCheckBox.isChecked = true
            holder.roomCard.isClickable = false
        } else {
            holder.roomStatus.text =
                context.getString(R.string.room_status).plus(" ")
                    .plus(context.getString(R.string.room_to_clean))
            context.theme.resolveAttribute(R.attr.colorError, typedValue, true)
            holder.roomStatus.setTextColor(typedValue.data)
            holder.roomCheckBox.isChecked = false
            holder.roomCard.isClickable = true

            holder.roomCard.setOnClickListener {

                if (!holder.roomCheckBox.isChecked) {
                    val cacheToken = File(context.cacheDir, "token")
                    var authorization = ""
                    if (cacheToken.exists()) {
                        authorization = cacheToken.readText()
                    }
                    loading.show()
                    viewModel.cleanRoom(authorization, holder.roomText.text.toString())
                }
            }
        }

        if (roomOpened[position]) {
            holder.roomOpen.text = context.getString(R.string.room_open)
            context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
            holder.roomOpen.setTextColor(typedValue.data)
        } else {
            holder.roomOpen.text = context.getString(R.string.room_closed)
            context.theme.resolveAttribute(R.attr.colorError, typedValue, true)
            holder.roomOpen.setTextColor(typedValue.data)
        }
    }

    fun cleanRoom(formResult: CleanRoomResult, roomStatus: TextView, roomCard: CardView, roomCheckBox: CheckBox) {
        loading.hide()
        if (formResult.success != null) {
            val typedValue = TypedValue()
            roomStatus.text =
                context.getString(R.string.room_status).plus(" ").plus(context.getString(R.string.room_to_clean))
            context.theme?.resolveAttribute(R.attr.colorPrimary, typedValue, true)
            roomStatus.setTextColor(typedValue.data)
            roomCheckBox.isChecked = true
            roomCard.isClickable = false
        }
    }

    override fun getItemCount(): Int {
        return roomList.size
    }
}
