package com.sweven.blockcovid.ui.rooms

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.sweven.blockcovid.R

class RoomsAdapter(ct: Context?, rl: Array<String>): RecyclerView.Adapter<RoomsAdapter.MyViewHolder>() {

    val context = ct
    private val roomList = rl

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var roomText: Button = itemView.findViewById(R.id.room_button)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.recycler_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.roomText.text = roomList[position]
    }

    override fun getItemCount(): Int {
        return roomList.size
    }
}