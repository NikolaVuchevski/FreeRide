package com.example.mpip.freeride.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.mpip.freeride.R;
import org.joda.time.DateTime;

import java.util.Calendar;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int day = intent.getIntExtra("startDay", 0);
        int hour = intent.getIntExtra("startHour", 0);
        int minute = intent.getIntExtra("startMin", 0);
        String month = intent.getStringExtra("startMonth");
        DateTime timerTest = DateTime.now();
        String msg = "You need to pick up your reserved bicycle on " + day + " " + month + " at " + String.format("%02d", hour) + ":" + String.format("%02d", minute) + "!";
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "n")
                .setSmallIcon(R.drawable.freeridelogo)
                .setSound(sound)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(msg))
                .setContentTitle("Reminder")
                .setTicker(msg)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(200, builder.build());
    }
}
