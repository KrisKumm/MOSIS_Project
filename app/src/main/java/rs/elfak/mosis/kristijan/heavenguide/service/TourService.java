package rs.elfak.mosis.kristijan.heavenguide.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import rs.elfak.mosis.kristijan.heavenguide.MapsActivity;
import rs.elfak.mosis.kristijan.heavenguide.ProfileActivity;
import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGroup;
import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class TourService extends Service {

    private static String CHANNEL_ID = "tour_notifications";
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    TourGroup tourGroup;
    ListenerRegistration listener;
    Timer timer;
    String myuid;
    String userType;
    String groupId;

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    public static final int DEFAULT_UPDATE_INTERVAL = 6;
    public static final int FAST_UPDATE_INTERVAL = 3;
    private static final int PERMISSIONS_FINE_LOCATION = 99;

    LocationRequest locationRequest;
    LocationCallback locationCallBack;

    @Override
    public void onCreate() {
        listener = null;

//        Intent notificationIntent = new Intent(this, MapsActivity.class);


//        android.app.Notification notification =
//                new Notification.Builder(this)
//                        .setContentTitle("kris voli")
//                        .setContentText("marijanova")
//                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//                        .setContentIntent(pendingIntent)
//                        .setTicker("muda")
//                        .build();
//
//        // Notification ID cannot be 0.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            groupId = intent.getExtras().getString("TOUR_GROUP");
            myuid = intent.getExtras().getString("MY_UID");
            userType = intent.getExtras().getString("USER_TYPE");

            if(groupId == null){
                stopSelf();
                return START_NOT_STICKY;
            }
        }

        getGroup(groupId);
        setOnGroupUpdateHandler(groupId);

        stopLocationUpdates();
        startLocationUpdates();
        //setGuideLocation();
        return START_STICKY;
    }

    private void setGuideLocation() {
        updateGPS();
        if(userType.equals("guide")) {
           timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(currentLocation != null){
                        updateGPS();
                        DBService.getInstance().UpdateGuideLocation(groupId,new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    }
                    else{
                        updateGPS();
                        DBService.getInstance().UpdateGuideLocation(groupId,new GeoPoint(0, 0));
                    }
                }

            }, 0, 3000);

        }
    }

    @SuppressLint("MissingPermission")
    private void updateGPS(){
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    fusedLocationProviderClient.getLastLocation().addOnSuccessListener( new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            currentLocation = location;
            if(currentLocation != null){
                UpdateGuideLocation(groupId,new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
            }
            else{
                UpdateGuideLocation(groupId,new GeoPoint(0, 0));
            }
        }
    })
    .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            updateGPS();
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

    private void setOnGroupUpdateHandler(String uid) {
        if (listener == null) {

            listener = DBService.getInstance().OnTourGroupUpdate(uid, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    tourGroup = (TourGroup) object;
                    if (userType.equals("tourist")) {
                        updateGPS();
                        if (currentLocation != null && Math.abs(tourGroup.getTourGuideLocation().getLatitude() - currentLocation.getLatitude()) < 0.1 && Math.abs(tourGroup.getTourGuideLocation().getLongitude() - currentLocation.getLongitude()) < 0.1) {
                            makeNotification("Izgubija si se", MapsActivity.class);
                        }
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
    @Override
    public void onDestroy() {

        if(groupId != null){
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("TOUR_GROUP", UserData.getInstance().tourGroupId);
            broadcastIntent.putExtra("MY_UID", UserData.getInstance().uId);
            broadcastIntent.putExtra("USER_TYPE", UserData.getInstance().userType.toString());
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, TourRestarted.class);
            this.sendBroadcast(broadcastIntent);
        }
        else{
            stopForeground(true);
        }
        if(listener != null){
            listener.remove();
        }
        super.onDestroy();


    }
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallBack = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateGPS();
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }
    private void stopLocationUpdates() {
        if(fusedLocationProviderClient != null)
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }
    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void UpdateGuideLocation(String id, GeoPoint location){
        final DocumentReference documentReference = fStore.collection("tour-groups").document(id);
        documentReference
                .update("tourGuideLocation", location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
