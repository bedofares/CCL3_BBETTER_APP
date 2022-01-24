package com.example.ccl3_bbetter_app.Reminder

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.ccl3_bbetter_app.MainActivity2
import com.example.ccl3_bbetter_app.R

const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"


class AlarmReceiver : BroadcastReceiver()
{
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent)
    {
        val RandomInt = intent.getIntExtra("RandomInt" , 0)
        val i = Intent(context, MainActivity2::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,RandomInt,i,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_notification_bell_svgrepo_com)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(RandomInt, notification)
    }

}