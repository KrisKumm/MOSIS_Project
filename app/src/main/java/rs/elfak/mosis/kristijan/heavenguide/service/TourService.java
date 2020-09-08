package rs.elfak.mosis.kristijan.heavenguide.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import rs.elfak.mosis.kristijan.heavenguide.MapsActivity;
import rs.elfak.mosis.kristijan.heavenguide.ProfileActivity;
import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGroup;

import static java.lang.Thread.sleep;

public class TourService extends Service {

    private static String CHANNEL_ID = "tour_notifications";
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    TourGroup tourGroup;
    ListenerRegistration listener;
    String myuid;


    @Override
    public void onCreate() {
        listener = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String groupId = intent.getExtras().getString("TOUR_GROUP");
        myuid = intent.getExtras().getString("MY_UID");


        getGroup(groupId);
        setOnGroupUpdateHandler(groupId);

        return START_STICKY;
    }
    @SuppressLint("MissingPermission")
    private void updateGPS(){
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    fusedLocationProviderClient.getLastLocation().addOnSuccessListener( new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
        currentLocation = location;
        }
        });
    }

    private void makeNotification(String message,final Class kuda) {
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "tour-service";
            notificationChannel = new NotificationChannel("tour_notifications", name, NotificationManager.IMPORTANCE_DEFAULT  );
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
                .setChannelId(CHANNEL_ID).build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        mNotificationManager.notify(1 , notification);
        getSystemService(Context.NOTIFICATION_SERVICE);

    }

    private void setOnGroupUpdateHandler(String uid){
        if(listener == null){
            listener = DBService.getInstance().OnTourGroupUpdate(uid, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    tourGroup = (TourGroup) object;
                    updateGPS();
                    //TODO ovde se proverava dal se poklapaju lokacije i salje se notifikacija ako se ne poklapaju

                    if(currentLocation != null && Math.abs(tourGroup.getTourGuideLocation().getLatitude() - currentLocation.getLatitude()) < 0.1 && Math.abs(tourGroup.getTourGuideLocation().getLongitude() - currentLocation.getLongitude())< 0.1){
                        makeNotification("Izgubija si se", MapsActivity.class);
                    }
                }
            });
        }
    }

    private void getGroup(String uid){
        DBService.getInstance().GetTourGroup(uid, new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                tourGroup = (TourGroup) object;
            }
        });
    }

    @Override
    public void onDestroy() {
        listener.remove();
        //TODO prekinuti rad servisa
        // isprazni niz pending tourists
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
