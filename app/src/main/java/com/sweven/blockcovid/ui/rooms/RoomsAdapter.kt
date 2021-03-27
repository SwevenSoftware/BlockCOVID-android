package com.sweven.blockcovid.ui.rooms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.sweven.blockcovid.R

class RoomsAdapter(ct: Context?, nc: NavController, rl: Array<String>, ro: Array<String>, rc: Array<String>, da: Array<Array<String>>, op: Array<Boolean>): RecyclerView.Adapter<RoomsAdapter.MyViewHolder>() {

    val context = ct
    private val navController = nc
    private val roomList = rl
    private val roomOpening = ro
    private val roomClosing = rc
    private val daysArray = da
    private val roomOpened = op


    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var roomText: TextView = itemView.findViewById(R.id.room_text)
        var desksTaken: TextView = itemView.findViewById(R.id.desks_taken)
        var roomOpen: TextView = itemView.findViewById(R.id.room_open)
        var roomCard: CardView = itemView.findViewById(R.id.room_card)

        // TextViews dei giorni
        var monday: TextView = itemView.findViewById(R.id.monday)
        var tuesday: TextView = itemView.findViewById(R.id.tuesday)
        var wednesday: TextView = itemView.findViewById(R.id.wednesday)
        var thursday: TextView = itemView.findViewById(R.id.thursday)
        var friday: TextView = itemView.findViewById(R.id.friday)
        var saturday: TextView = itemView.findViewById(R.id.saturday)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.recycler_row_user, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.roomText.text = roomList[position]
        holder.desksTaken.text =
            roomOpening[position].plus(" - ").plus(roomClosing[position])
        if (roomOpened[position]) {
            holder.roomOpen.text = context?.getString(R.string.room_open)
            if (context != null) {
                holder.roomOpen.setTextColor(context.getColor(R.color.green_500))
            }
        } else {
            holder.roomOpen.text = context?.getString(R.string.room_closed)
            if (context != null) {
                holder.roomOpen.setTextColor(context.getColor(R.color.red_600))
            }
        }
        if (context != null) {
            for (i in daysArray[position].indices) {
                when (daysArray[position][i]) {
                    "MONDAY" -> holder.monday.setTextColor(context.getColor(R.color.green_500))
                    "TUESDAY" -> holder.tuesday.setTextColor(context.getColor(R.color.green_500))
                    "WEDNESDAY" -> holder.wednesday.setTextColor(context.getColor(R.color.green_500))
                    "THURSDAY" -> holder.thursday.setTextColor(context.getColor(R.color.green_500))
                    "FRIDAY" -> holder.friday.setTextColor(context.getColor(R.color.green_500))
                    "SATURDAY" -> holder.saturday.setTextColor(context.getColor(R.color.green_500))
                }
            }
        }
        holder.roomCard.setOnClickListener {
            if(holder.roomOpen.text != context?.getString(R.string.room_open)) {
                val roomName = holder.roomText.text.toString()
                val action = RoomsFragmentDirections.actionNavigationRoomsToNavigationRoomView(roomName)
                navController.navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return roomList.size
    }
}