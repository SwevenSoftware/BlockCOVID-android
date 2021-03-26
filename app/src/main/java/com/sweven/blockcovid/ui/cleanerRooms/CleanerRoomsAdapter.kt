package com.sweven.blockcovid.ui.cleanerRooms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sweven.blockcovid.R

class CleanerRoomsAdapter(ct: Context?, rl: Array<String>, rc: Array<Boolean>): RecyclerView.Adapter<CleanerRoomsAdapter.MyViewHolder>() {

    val context = ct
    private val roomList = rl
    private val roomCleaned = rc

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
                context?.getString(R.string.room_status).plus(" ").plus(context?.getString(R.string.room_cleaned))
            holder.roomCheckBox.isChecked = true
        } else {
            holder.roomStatus.text =
                context?.getString(R.string.room_status).plus(" ").plus(context?.getString(R.string.room_to_clean))
        }
        holder.roomCard.setOnClickListener {
            if (holder.roomCheckBox.isChecked) {
                holder.roomCheckBox.isChecked = false
                holder.roomStatus.text =
                        context?.getString(R.string.room_status).plus(" ").plus(context?.getString(R.string.room_to_clean))
            } else {
                holder.roomCheckBox.isChecked = true
                holder.roomStatus.text =
                        context?.getString(R.string.room_status).plus(" ").plus(context?.getString(R.string.room_cleaned))
            }
        }
    }

    override fun getItemCount(): Int {
        return roomList.size
    }
}