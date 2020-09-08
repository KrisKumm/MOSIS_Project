package rs.elfak.mosis.kristijan.heavenguide.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.ProfileActivity;
import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;

public class NotificationService extends Service {

    ListenerRegistration listener;
    String myuid;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myuid = intent.getExtras().getString("MY_UID");
        setNotificationsUpdateHandler();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        listener.remove();
    }
    private void setNotificationsUpdateHandler(){
        if(listener == null){
            listener = DBService.getInstance().OnNotificationUpdate(DBService.getInstance().GetUserReference(myuid), new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                   Notification newNotification = (Notification) object;
                        makeNotification(newNotification.getMessage());
                }
            });
        }
    }
    private void makeNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.baseline_message_black_18dp)
                .setContentTitle("New Notification")
                .setContentText(message)
                .setAutoCancel(true);

        Intent i = new Intent(this, ProfileActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 , builder.build());

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
