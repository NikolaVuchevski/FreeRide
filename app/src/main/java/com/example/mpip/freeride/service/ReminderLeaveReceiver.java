package com.example.mpip.freeride.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.mpip.freeride.R;

public class ReminderLeaveReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int day = intent.getIntExtra("endDay", 0);
        int hour = intent.getIntExtra("endHour", 0);
        int minute = intent.getIntExtra("endMin", 0);
        String month = intent.getStringExtra("endMonth");
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String msg = "You need to leave your reserved bicycle on " + day + " " + month + " at " + String.format("%02d", hour) + ":" + String.format("%02d", minute) + "!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "n1")
                .setSmallIcon(R.drawable.freeridelogo)
                .setSound(sound)
                .setContentTitle("Reminder")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setTicker(msg)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(200, builder.build());
    }
}
