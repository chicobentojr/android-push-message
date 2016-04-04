package br.com.chicobentojr.androidpushmessage.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

import br.com.chicobentojr.androidpushmessage.R;
import br.com.chicobentojr.androidpushmessage.activitys.MainActivity;
import br.com.chicobentojr.androidpushmessage.models.Message;
import br.com.chicobentojr.androidpushmessage.utils.AppController;
import de.greenrobot.event.EventBus;

/**
 * Created by Francisco on 01/04/2016.
 */
public class MyGcmListenerService extends GcmListenerService {
    public static final String LOG = "LOG";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        //super.onMessageReceived(from, data);

        Message message = new Gson().fromJson(data.getString("message"), Message.class);

        if (AppController.isMyApplicationTaskOnTop(this)) {
            EventBus.getDefault().post(message);
        } else {
            sendNotification(message);
        }
    }

    public void sendNotification(Message message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("message", message);

        String summary = message.User.Name + " @" + message.User.Login;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setTicker(message.Title)
                .setSmallIcon(R.drawable.ic_launcher)
                        //.setColor(getResources().getColor(R.color.colorPrimary))
                        //.setContentTitle(message.Title)
                        //.setContentText(message.Content)
                .setAutoCancel(true)
                        //.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        //.setPriority(Notification.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.InboxStyle()
                        .setBigContentTitle(message.Title)
                        .addLine(message.Content)
                        .setSummaryText(summary))
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        notificationManager.notify(2699, builder.build());

    }
}
