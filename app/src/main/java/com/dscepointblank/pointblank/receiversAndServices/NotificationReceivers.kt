package com.dscepointblank.pointblank.receiversAndServices

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.dscepointblank.pointblank.utilityClasses.MyNotifications

class NotificationReceivers : BroadcastReceiver() {

    private lateinit var  notificationManager : NotificationManager
    private  var context:Context? = null;
    override fun onReceive(context: Context?, intent: Intent?) {

        this.context = context
        notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createNotificationChannel(
                "com.ebookfrenzy.notifydemo.news",
                "NotifyDemo News",
                "Example News Channel")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {

        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern =
            longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(channel)
        val channelID = "com.ebookfrenzy.notifydemo.news"

        val notification = Notification.Builder(context,
            channelID)
            .setContentTitle("Example Notification")
            .setContentText("This is an  example notification.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setChannelId(channelID)
            .build()

        Log.e("DDDD","I am here")
        notificationManager.notify(500, notification)

        val notify = context?.let {
            MyNotifications(
                it
            )
        }
        notify?.createNotification()
    }
}