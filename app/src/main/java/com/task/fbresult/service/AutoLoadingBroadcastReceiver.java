package com.task.fbresult.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.task.fbresult.MainActivity;
import com.task.fbresult.R;
import com.task.fbresult.model.AlertDTO;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.WebUtils;

import java.io.Serializable;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.single.SingleDoAfterTerminate;


@RequiresApi(api = Build.VERSION_CODES.R)
public class AutoLoadingBroadcastReceiver extends BroadcastReceiver {

    private static final int NOTIFY_ID = 12;
    private static final long REAPEATING_TIME_IN_MILLIS = 15_000;
    public static final String CURRENT_USER_TAG = "current_fb_user";
    private static final String channelId = "Your_channel_id";
    final String LOG_TAG = "myReceiverLogs";
    private Context context;
    private Person currentUser;

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.d(LOG_TAG, "onReceive " + intent.getAction());

        setCurrentUser(intent);
        Log.d(LOG_TAG, "onReceive user " + currentUser);
        //TODO uncomment

//        checkAlert();
//        checkNewChangeMessage();
//        setNext(context, intent);
        createNotification("","",0); //delete it (only for testing)
    }

    private void setCurrentUser(Intent intent){
        currentUser = FBUtils.getCurrentUserAsPerson();
    }
    private void checkNewChangeMessage() {
        if(isNewUnreadChangeMessage()){
            createNotification(context.getString(R.string.change_notification_title), "", 1);
        }
    }

    private boolean isNewUnreadChangeMessage() {
        //todo check from db

        return false;
    }

    private void checkAlert() {
        WebUtils.peekAlert(FBUtils.getCurrentUserAsPerson(),handle, (e)->{} );
    }

    Consumer<List<AlertDTO>> handle = (response) ->{
        if(!response.isEmpty()){
            for (int i = 0; i < response.size(); i++) {
                AlertDTO alertDTO = response.get(i);
                createNotification(alertDTO.getType(), alertDTO.getMessage(), i);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void createNotification(String title, String content, int id) {



        Uri ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        long[] vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 };
        NotificationManager nf = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{intent}, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setVibrate(vibrate)
                .setContentText(content)
/*                .setFlag(Notification.FLAG_INSISTENT, true)
                .setFlag(Notification.FLAG_SHOW_LIGHTS, true)*/
                .setLights(Color.RED, 1,0)
                .setSound(ringURI)
                .setStyle(new Notification.BigTextStyle().bigText(content))
                .setAutoCancel(false) // автоматически закрыть уведомление после нажатия if true
                .setSmallIcon(R.mipmap.ic_launcher);

        Toast.makeText(context, "new on res", Toast.LENGTH_SHORT).show();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            nf.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        nf.notify(NOTIFY_ID + id, builder.build());
    }

    private void setNext(Context context, Intent intent) {
        long time = System.currentTimeMillis() + REAPEATING_TIME_IN_MILLIS;
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}
