package rs.elfak.mosis.kristijan.heavenguide.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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

    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    TourGroup tourGroup;
    ListenerRegistration listener;
    ArrayList<Notification> notifications = null;

    @Override
    public void onCreate() {
        listener = null;
        DBService.getInstance().getNotifications(DBService.getInstance().GetUserReference(UserData.getInstance().uId), new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                notifications = (ArrayList<Notification>) object;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String groupId = intent.getExtras().getString("TOUR_GROUP");
        getGroup(groupId);
        setOnGroupUpdateHandler(groupId);

        return START_STICKY;
    }
    @SuppressLint("MissingPermission")
    private void updateGPS(){
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Executor) this, new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
        currentLocation = location;
        }
        });
    }

    private void makeNotification(String message,final Class kuda) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.baseline_message_black_18dp)
                .setContentTitle("new Tour Notification")
                .setContentText(message)
                .setAutoCancel(true);

        Intent i = new Intent(this, kuda);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 , builder.build());

    }

    private void setOnGroupUpdateHandler(String uid){
        if(listener == null){
            listener = DBService.getInstance().OnTourGroupUpdate(uid, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    tourGroup = (TourGroup) object;
                    updateGPS();
                    DBService.getInstance().getNotifications(DBService.getInstance().GetUserReference(UserData.getInstance().uId), new FirebaseCallback() {
                        @Override
                        public void onCallback(Object object) {
                            if(notifications.size() < ((ArrayList<Notification>) object).size())
                                makeNotification("New Notification", ProfileActivity.class);
                        }
                    });
                    //TODO ovde se proverava dal se poklapaju lokacije i salje se notifikacija ako se ne poklapaju
                    if(Math.abs(tourGroup.getTourGuideLocation().getLatitude() - currentLocation.getLatitude()) < 0.1 && Math.abs(tourGroup.getTourGuideLocation().getLongitude() - currentLocation.getLongitude())< 0.1){
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
