package com.example.todolist.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todolist.R
import com.example.todolist.activities.MainActivity

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
const val openFragment = "TaskFragment"
const val task_id = "1"

class Notification: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val tempIntent = Intent(context,MainActivity::class.java)
        tempIntent.putExtra(openFragment,intent.getStringExtra(openFragment))
        tempIntent.putExtra(task_id,intent.getStringExtra(task_id))
        val tempPending = PendingIntent.getActivity(
            context,
            0,
            tempIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
            val notification = NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra))
                .setContentIntent(tempPending)
                .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(intent.getIntExtra(notificationID.toString(),1),notification)
    }
}