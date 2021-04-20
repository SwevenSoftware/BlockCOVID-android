package com.sweven.blockcovid.ui.userReservations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.sweven.blockcovid.R

class UserReservationsAdapter(ct: Context?, nc: NavController, ri: Array<String>, di: Array<String>, sa: Array<String>, ea: Array<String>, da: Array<String>): RecyclerView.Adapter<UserReservationsAdapter.MyViewHolder>() {

    val context = ct
    private val navController = nc
    private val reservationsId = ri
    private val desksId = di
    private val startTimes = sa
    private val endTimes = ea
    private val daysArray = da

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var reservationCard: CardView = itemView.findViewById(R.id.reservation_card)
        var reservationDesk: TextView = itemView.findViewById(R.id.edit_reservation_desk)
        var reservationTime: TextView = itemView.findViewById(R.id.edit_reservation_time)
        var reservationDate: TextView = itemView.findViewById(R.id.edit_reservation_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.recycler_row_reservation, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.reservationDesk.text = desksId[position]
        holder.reservationTime.text =
            startTimes[position].plus(" - ").plus(endTimes[position])
        holder.reservationDate.text = daysArray[position]
        holder.reservationCard.setOnClickListener {
            val reservationId = reservationsId[position]
            //val action = UserReservationsFragmentDirections.actionNavigationUserRoomsToNavigationRoomView(roomName)
            //navController.navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return reservationsId.size
    }
}