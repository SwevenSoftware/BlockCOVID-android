package com.sweven.blockcovid.ui.rooms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sweven.blockcovid.R

class RoomsAdapter(ct: Context?, rl: Array<String>, rs: Array<String>): RecyclerView.Adapter<RoomsAdapter.MyViewHolder>() {

    val context = ct
    private val roomList = rl
    private val roomStatus = rs

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var roomText: TextView = itemView.findViewById(R.id.room_text)
        var desksTaken: TextView = itemView.findViewById(R.id.desks_taken)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.recycler_row_user, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.roomText.text = roomList[position]
        holder.desksTaken.text =
            context?.getString(R.string.desks_taken).plus(" ").plus(roomStatus[position])
    }

    override fun getItemCount(): Int {
        return roomList.size
    }
}