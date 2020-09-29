package rs.elfak.mosis.kristijan.heavenguide.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.ProfileActivity;
import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.ui.notifications.NotificationsFragment;

public class NotificationService extends Service {

    private static String CHANNEL_ID = "notifications" ;
    ListenerRegistration listener;
    String myuid;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(myuid == null && intent != null)
            if(intent != null){
                myuid = intent.getExtras().getString("MY_UID");
                setNotificationUpdateHandler();
            }

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent intent = new Intent("com.android.ServiceStopped");
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        listener.remove();
    }
    private void setNotificationUpdateHandler(){
        if(listener == null){
            listener = DBService.getInstance().OnNotificationUpdate(getUserReference(), new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                   Notification newNotification = (Notification) object;
                        makeNotification(newNotification.getMessage());
                }
            });
        }
    }
    private void makeNotification(String message) {
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifications";
            notificationChannel = new NotificationChannel("notifications", name, NotificationManager.IMPORTANCE_DEFAULT  );
            CHANNEL_ID = notificationChannel.getId();
        }
        Intent i = new Intent(this, ProfileActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        android.app.Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_message_black_18dp)
                .setContentTitle("New Notification")
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setLights(Color.parseColor("cyan"), 500, 3000)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setChannelId(CHANNEL_ID).build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        mNotificationManager.notify(1 , notification);

    }
    private DocumentReference getUserReference(){
        if(UserData.getInstance().userType.toString().equals("tourist"))
            return DBService.getInstance().GetUserReference(UserData.getInstance().uId);
        else if(UserData.getInstance().userType.toString().equals("guide"))
            return DBService.getInstance().GetGuideReference(UserData.getInstance().uId);
        else
            return DBService.getInstance().GetManagerReference(UserData.getInstance().uId);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
