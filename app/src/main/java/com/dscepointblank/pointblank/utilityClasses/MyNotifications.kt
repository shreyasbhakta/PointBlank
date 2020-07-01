package com.dscepointblank.pointblank.utilityClasses

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.dscepointblank.pointblank.receiversAndServices.NotificationReceivers
import java.util.*

class MyNotifications(context :Context) {
    private var context:Context? = null

    init {
        this.context = context;
    }

    fun createNotification(){
        var alarmManager : AlarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,
            NotificationReceivers::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);

        val cal = Calendar.getInstance()

        cal.set(Calendar.MINUTE,cal.get(Calendar.MINUTE)+2)
        cal.set(Calendar.SECOND,0)
        AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager,AlarmManager.RTC_WAKEUP,cal.timeInMillis,pendingIntent)

    }
}