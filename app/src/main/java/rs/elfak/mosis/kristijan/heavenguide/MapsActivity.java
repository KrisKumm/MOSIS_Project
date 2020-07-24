package rs.elfak.mosis.kristijan.heavenguide;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int DEFAULT_UPDATE_INTERVAL = 4;
    public static final int FAST_UPDATE_INTERVAL = 2;
    private static final int PERMISSIONS_FINE_LOCATION = 99;

    private GoogleMap mMap;

    Switch sw_locationsupdates, sw_gps;

    // variable to remember if we are tracking location or not
    boolean updateOn = false;

    // current location
    Location currentLocation;

    // list of saved locations
    List<Location> savedLocations;

    // Location request is a config file for all settings related to FusedLocationProviderClient
    LocationRequest locationRequest;

    LocationCallback locationCallBack;

    // Google's API fot location services. The Majority of the app functions using this class.
    FusedLocationProviderClient fusedLocationProviderClient;

    //Global marker for my location
    public Marker meMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        MyAppSingleton myApplication = (MyAppSingleton)getApplicationContext();
//        savedLocations = myApplication.getMyLocations();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//         //Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng start = new LatLng(43.3209, 21.8958);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 14f));

        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

//        for(Location location: savedLocations){
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Lat:" + location.getLatitude() + " Long:" + location.getLongitude());
//            mMap.addMarker(markerOptions);
//        }

        LatLng home = new LatLng(43.320922, 21.894367);
        mMap.addMarker(
                new MarkerOptions()
                        .position(home)
                        .title("Kris")
                        .snippet("It me")
                        //.icon(bitmapDescriptorFromVector(getApplicationContext(),R.mipmap.ic_me_icon2_round))
        );
        mMap.addCircle(
                new CircleOptions()
                        .center(home)
                        .radius(10.0)
                        .strokeWidth(2f)
                        .strokeColor(Color.BLACK)
                        .fillColor(Color.argb(70, 200, 80, 80))
        );

        LatLng tvrdjavaCentar = new LatLng(43.325864, 21.895371);
        LatLng tvrdjava1 = new LatLng(43.3236536, 21.8959328);
        LatLng tvrdjava2 = new LatLng(43.325232, 21.892303);
        LatLng tvrdjava3 = new LatLng(43.326616, 21.892664);
        LatLng tvrdjava4 = new LatLng(43.327764, 21.895554);
        LatLng tvrdjava5 = new LatLng(43.326924, 21.898051);
        LatLng tvrdjava6 = new LatLng(43.325790, 21.898403);
        mMap.addMarker(
                new MarkerOptions()
                        .position(tvrdjavaCentar)
                        .title("Tvrdjava")
                        .snippet("Centralna tacka tvrdjave")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        );
        mMap.addPolygon(
                new PolygonOptions()
                        .add(tvrdjava1, tvrdjava2, tvrdjava3, tvrdjava4, tvrdjava5, tvrdjava6, tvrdjava1)
                        .strokeWidth(5f)
                        .strokeColor(Color.BLACK)
                        .fillColor(Color.argb(70, 80, 80, 200))
        );

        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);

        //set all properties of LocationRequest
        locationRequest = new LocationRequest();
        //how often does the default location check  occur?
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        //how often does the location check occur when set to the most frequent update?
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //event that is triggered whenever the update interval is met.
        locationCallBack = new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                // save the location
                if(meMarker!=null){
                    meMarker.remove();
                }
                updateGPS();
            }
        };

        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()){
                    // most accurate - use GPS
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                }
                else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                }
            }
        });

        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationsupdates.isChecked()){
                    // turn on location tracking
                    startLocationUpdates();
                }
                else {
                    // turn off tracking
                    stopLocationUpdates();
                }
            }
        });

        updateGPS();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }
                else{
                    Toast.makeText(this, "This app requires permission to be granted in order to work properly!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void updateGPS(){
        // get permissions from the user to track GPS
        // get the current location from the fused client
        // update the UI - i.e. set all properties in their associated text view items.

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // user provided the permisssion

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // we got permissions. Put the values of location. XXX into the UI components.

                    currentLocation = location;

                    if(meMarker!=null){
                        meMarker.remove();
                    }

                    LatLng meLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    meMarker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(meLatLng)
                                    .title("Kris")
                                    .snippet("It me")
                                    .icon(bitmapDescriptorFromVector(getApplicationContext(),R.mipmap.ic_me_icon2_round))
                    );
                }
            });
        }
        else {
            // permissions not granted yet.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }
}