package rs.elfak.mosis.kristijan.heavenguide;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
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
import com.google.common.collect.Maps;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Star;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGroup;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.TourService;
import rs.elfak.mosis.kristijan.heavenguide.ui.login.LoginActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int DEFAULT_UPDATE_INTERVAL = 6;
    public static final int FAST_UPDATE_INTERVAL = 3;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private boolean tourBegun;

    private GoogleMap mMap;

    //Global marker for my location
    public Marker meMarker;
    //Global marker for my guide of current tour
    public Marker guideMarker;
    //Global marker list of markers for my friends
    public ArrayList<Marker> friendsMarker = new ArrayList<Marker>();;

    //Global attraction list for the ones in my radius
    public ArrayList<Attraction> attractionsAroundMe = new ArrayList<Attraction>();;
    public ArrayList<Marker> attractionsAroundMeMarkers = new ArrayList<Marker>();

    //Global stars list for star markers in current tour
    public ArrayList<Star> starsCurrentTour  = new ArrayList<Star>();;
    public ArrayList<Marker> starsMarkersCurrentTour  = new ArrayList<Marker>();;

    private Switch sw_locationsupdates, sw_gps;

    private ImageButton addNewStarButton;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private EditText popUpStarSnippet;
    private ImageButton popUpStarImageButton;
    private Button popUpStarCreateButton;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView popUpStarImageCamera;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_IMAGE_CAPTURE = 0;

    private ImageButton addNewStarButton;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private EditText popUpStarSnippet;
    private ImageButton popUpStarImageButton;
    private Button popUpStarCreateButton;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView popUpStarImageCamera;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_IMAGE_CAPTURE = 0;

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
        tourBegunCheck();
        //tourServiceIO();
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

        LatLng start = new LatLng(43.3209, 21.8958);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 14f));
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

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
                //getAttractions();
            }
        };

        mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.equals(meMarker)){
                    Intent i = new Intent(MapsActivity.this, ProfileActivity.class);
                    //i.putExtra("ATTRACTION","yyDEErmCMJvZPJov00NT");
                    startActivity(i);
                    //finish();
                    return true;
                }
                if(marker.getSnippet().equals("Attraction")){
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    marker.showInfoWindow();
                    return true;
                }
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker.getSnippet().equals("Attraction")){
                    for (Attraction attraction: attractionsAroundMe) {
                        if(attraction.getName().equals(marker.getTitle()))
                        {
                            UserData.getInstance().attraction = attraction;
                            UserData.getInstance().attractionPhoto = null;
                            Intent i = new Intent(MapsActivity.this, AttractionActivity.class);
                            startActivity(i);
                        }
                    }
                }
            }
        });


        createMeMarks();
        createRegions();
        setSwitches();
        setStarButton();
        updateGPS();
    }

    private void getAttractions() {
        GeoPoint topLeft = new GeoPoint(meMarker.getPosition().latitude + 0.1, meMarker.getPosition().longitude - 0.1);
        GeoPoint bottomRight = new GeoPoint(meMarker.getPosition().latitude - 0.1, meMarker.getPosition().longitude + 0.1);
        DBService.getInstance().GetAttractionsByLocation(topLeft, bottomRight, new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                attractionsAroundMe = (ArrayList<Attraction>) object;
                drawAttractionsMarker();
            }
        });
    }

    private void drawAttractionsMarker() {
        for(Attraction attraction: attractionsAroundMe){
            LatLng latLng = new LatLng(attraction.getLocation().getLatitude(), attraction.getLocation().getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(attraction.getName());
            markerOptions.snippet("Attraction");
            Marker mark = mMap.addMarker(markerOptions);
            attractionsAroundMeMarkers.add(mark);
        }
    }

    private void removeAttractionsMarker(){
        for(Marker marker: attractionsAroundMeMarkers){
            marker.remove();
        }
    }

    private void setSwitches() {
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);
        sw_gps.setChecked(true);
        sw_locationsupdates.setChecked(true);
//        sw_gps.setVisibility(View.INVISIBLE);
//        sw_locationsupdates.setVisibility(View.INVISIBLE);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        startLocationUpdates();
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
    }

    private void setStarButton(){
        addNewStarButton = findViewById(R.id.add_new_star_button);
        addNewStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                final View createStarPopUp = getLayoutInflater().inflate(R.layout.popup_new_star, null);
                popUpStarSnippet = createStarPopUp.findViewById(R.id.popup_new_star_snippet);
                popUpStarImageButton = createStarPopUp.findViewById(R.id.popup_new_star_image_button);
                popUpStarCreateButton = createStarPopUp.findViewById(R.id.popup_new_star_create_button);
                popUpStarImageButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
//                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
//                        }
//                        else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                        }
                    }
                });
                popUpStarCreateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LatLng starLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(starLocation);
                        markerOptions.title("Star");
                        markerOptions.snippet(popUpStarSnippet.toString());
                        Marker mark = mMap.addMarker(markerOptions);
                        dialog.dismiss();
                    }
                });
                dialogBuilder.setView(createStarPopUp);
                dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }

    private void setStarButton(){
        addNewStarButton = findViewById(R.id.add_new_star_button);
        addNewStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                final View createStarPopUp = getLayoutInflater().inflate(R.layout.popup_new_star, null);
                popUpStarSnippet = createStarPopUp.findViewById(R.id.popup_new_star_snippet);
                popUpStarImageButton = createStarPopUp.findViewById(R.id.popup_new_star_image_button);
                popUpStarCreateButton = createStarPopUp.findViewById(R.id.popup_new_star_create_button);
                popUpStarImageButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        }
                        else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                });
                popUpStarCreateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LatLng starLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(starLocation);
                        markerOptions.title("Star");
                        markerOptions.snippet(popUpStarSnippet.toString());
                        Marker mark = mMap.addMarker(markerOptions);
                        dialog.dismiss();
                    }
                });
                dialogBuilder.setView(createStarPopUp);
                dialog = dialogBuilder.create();
                dialog.show();
            }
        });
    }

    private void createRegions() {
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
    }

    private void createMeMarks() {
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
            case MY_CAMERA_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                else {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            popUpStarImageCamera.setImageBitmap(photo);
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
                    if(!attractionsAroundMeMarkers.isEmpty()){
                        removeAttractionsMarker();
                    }
                    getAttractions();
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
    private void tourBegunCheck() {
        if(UserData.getInstance().tourGroupId != null)
            tourBegun = true;
        else
            tourBegun = false;
    }
    private void tourServiceIO() {
        if(tourBegun){
            Intent service = new Intent(this, TourService.class);
            service.getExtras().putString("TOUR_GROUP", UserData.getInstance().tourGroupId);
            startService(service);
        }
        else{
            stopService(new Intent(this, TourService.class));
        }

    }
    public void onGroupUpdate(String id){
        final DocumentReference docRef = fStore.collection("tour-groups").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("TAG", "Current data: " + snapshot.toObject(TourGroup.class));
                    TourGroup tgUpdate =  snapshot.toObject(TourGroup.class);
                    // U TOUR GROUP SE NALAZI LOKACIJA VODICA I KAD SE PROMENI OVO SE POZIVA
                    // ILI KAD SE PROMENI OVO readyAll
                    // TAKO DA MOZEMO DA TURIMO ONE ZVEZDICE I BILO KAD KAD SE DODA NEKA NOVA DA SAMO PRIKAZEMO NA MAPI
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });
    }
}