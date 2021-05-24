package com.sweven.blockcovid

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationPublisher : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        // Create an explicit intent for an Activity in your app
        val startIntent = Intent(context, StartActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, startIntent, 0)

        // Builder for the "Reservation ended" notification
        val builder = NotificationCompat.Builder(context, "notifyEndChannel")
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(context.getString(R.string.reservation_almost_ended))
            .setContentText(context.getString(R.string.reservation_ended_long))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

        notificationManager.notify(200, builder.build())
    }
}
