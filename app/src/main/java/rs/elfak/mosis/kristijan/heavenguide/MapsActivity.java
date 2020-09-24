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
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
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
import com.google.firebase.firestore.ListenerRegistration;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Star;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGroup;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;
import rs.elfak.mosis.kristijan.heavenguide.service.TourService;
import rs.elfak.mosis.kristijan.heavenguide.ui.login.LoginActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PROFILE = "tourist";
    public String profileP;

    public static final int DEFAULT_UPDATE_INTERVAL = 6;
    public static final int FAST_UPDATE_INTERVAL = 3;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private boolean tourBegun;
    private TourGroup tourGroup = null;
    private Bitmap guidePhoto;
    private GoogleMap mMap;

    //Global marker for my location
    public Marker meMarker;
    //Global marker for my guide of current tour
    public Marker guideMarker;
    //Global marker list of markers for my friends
    public ArrayList<Marker> friendsMarker = new ArrayList<Marker>();

    //Global attraction list for the ones in my radius
    public ArrayList<Attraction> attractionsAroundMe = new ArrayList<Attraction>();
    public ArrayList<Marker> attractionsAroundMeMarkers = new ArrayList<Marker>();

    //Global stars list for star markers in current tour
    public ArrayList<Star> starsCurrentTour  = new ArrayList<Star>();
    public ArrayList<Marker> starsMarkersCurrentTour  = new ArrayList<Marker>();

    private ImageButton addNewStarButton;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private EditText popUpStarSnippet;
    private ImageButton popUpStarImageButton;
    private Button popUpStarCreateButton;
    private TextView popUpShowStarSnippet;
    private ImageView popUpShowStarImage;

    public ListenerRegistration groupListener = null;
    public ListenerRegistration starsListener = null;

    private Switch sw_locationsupdates, sw_gps;

    private static final int CAMERA_REQUEST = 1888;
    private ImageView popUpStarImageCamera;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_IMAGE_CAPTURE = 0;

    private static final int RESULT_LOAD_IMAGE = 1; // Za gallery
    public Bitmap picture;
    public Context context;
    public int activity = 4;

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

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        profileP = sharedPreferences.getString(PROFILE, "");

//        MyAppSingleton myApplication = (MyAppSingleton)getApplicationContext();
//        savedLocations = myApplication.getMyLocations();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng start = new LatLng(43.3314676, 21.883697);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 14f));
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        setLocationRequestAndCallback();
        setSwitches();
        setMarkerListeners();
        createRegions();
        setStarButton();
        tourBegunCheck();
        updateGPS();
    }

    private void setLocationRequestAndCallback(){
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

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    private void setMarkerListeners(){
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
                if(marker.getTitle().equals("Star")){
                    dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
                    final View showStarPopUp = getLayoutInflater().inflate(R.layout.popup_star_view, null);
                    popUpShowStarSnippet = showStarPopUp.findViewById(R.id.popup_star_view_snippet);
                    popUpShowStarImage = showStarPopUp.findViewById(R.id.popup_star_view_image);
                    popUpShowStarSnippet.setText(marker.getSnippet());
                    StorageService.getInstance().downloadPhoto("tour-group", UserData.getInstance().tourGroupId, marker.getTag().toString(), new FirebaseCallback() {
                        @Override
                        public void onCallback(Object object) {
                            popUpShowStarImage.setImageBitmap(Bitmap.createScaledBitmap((Bitmap) object, 200, 200, false));
                        }
                    });
                    dialogBuilder.setView(showStarPopUp);
                    dialog = dialogBuilder.create();
                    dialog.show();
                }
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
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        );
        mMap.addPolygon(
                new PolygonOptions()
                        .add(tvrdjava1, tvrdjava2, tvrdjava3, tvrdjava4, tvrdjava5, tvrdjava6, tvrdjava1)
                        .strokeWidth(5f)
                        .strokeColor(Color.BLACK)
                        .fillColor(Color.argb(70, 80, 80, 200))
        );

        LatLng CegarKamenica1 = new LatLng(43.352926, 21.943050);
        LatLng CegarKamenica2 = new LatLng(43.365382, 21.965142);
        LatLng CegarKamenica3 = new LatLng(43.379051, 21.942874);
        LatLng CegarKamenica4 = new LatLng(43.367385, 21.928685);
        mMap.addPolygon(
                new PolygonOptions()
                        .add(CegarKamenica1, CegarKamenica2, CegarKamenica3, CegarKamenica4, CegarKamenica1)
                        .strokeWidth(5f)
                        .strokeColor(Color.BLACK)
                        .fillColor(Color.argb(70, 80, 80, 200))
        );

        LatLng brzibrod1 = new LatLng(43.315479, 21.939550);
        LatLng brzibrod2 = new LatLng(43.306585, 21.937801);
        LatLng brzibrod3 = new LatLng(43.310934, 21.975312);
        LatLng brzibrod4 = new LatLng(43.302136, 21.973415);
        mMap.addPolygon(
                new PolygonOptions()
                        .add(brzibrod1, brzibrod2, brzibrod4, brzibrod3, brzibrod1)
                        .strokeWidth(5f)
                        .strokeColor(Color.BLACK)
                        .fillColor(Color.argb(70, 80, 80, 200))
        );
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
                popUpStarImageCamera = new ImageView(MapsActivity.this);
                popUpStarImageButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
//                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                    }
                });
                popUpStarCreateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BitmapDrawable drawable = (BitmapDrawable) popUpStarImageCamera.getDrawable();
                        Bitmap bitmapStar = drawable.getBitmap();
                        if (currentLocation != null && !popUpStarSnippet.getText().toString().isEmpty() && bitmapStar != null) {
                            GeoPoint starGeoPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                            String randNumber = getRandomNumber();
                            Star newStar = new Star(null, popUpStarSnippet.getText().toString(), randNumber, starGeoPoint, randNumber);
                            DBService.getInstance().AddStar(DBService.getInstance().GetTourGroupReference(UserData.getInstance().tourGroupId), newStar);
                            StorageService.getInstance().uploadPhoto("tour-group", UserData.getInstance().tourGroupId, randNumber, bitmapStar, MapsActivity.this);
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(MapsActivity.this, "Some field is empty or your location is off", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogBuilder.setView(createStarPopUp);
                dialog = dialogBuilder.create();
                dialog.show();
            }
        });

    }

    private void getAttractions() {
        GeoPoint topLeft = new GeoPoint(meMarker.getPosition().latitude + 3, meMarker.getPosition().longitude - 3);
        GeoPoint bottomRight = new GeoPoint(meMarker.getPosition().latitude - 3, meMarker.getPosition().longitude + 3);
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
//            case MY_CAMERA_PERMISSION_CODE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                }
//                else {
//                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
//                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 3){
            finish();
            startActivity(getIntent());
        }
        if (requestCode == activity) {
            //popuniPolja();
        }
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE  && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            popUpStarImageCamera.setImageURI(selectedImageUri);
            try {
                picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

            }
            catch(Exception e){
                e.printStackTrace();
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

                    LatLng meLatLng;
                    if(currentLocation != null)
                        meLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    else
                        meLatLng = new LatLng(0, 0);
                    meMarker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(meLatLng)
                                    .title("Kris")
                                    .snippet("It me")
                                    .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(UserData.getInstance().portrait,  100, 100, false))));

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
        {
            tourBegun = true;
            addNewStarButton.setVisibility(View.VISIBLE);
        }
        else {
            tourBegun = false;
            addNewStarButton.setVisibility(View.GONE);
        }
        tourServiceIO();
    }

    private void tourServiceIO() {
        if(tourBegun)
            startTour();

        Intent service = new Intent(this, TourService.class);
        service.putExtra("TOUR_GROUP", UserData.getInstance().tourGroupId);
        service.putExtra("MY_UID", UserData.getInstance().uId);
        service.putExtra("USER_TYPE", UserData.getInstance().userType.toString());
        startService(service);


    }

    public void startTour(){
        onGroupUpdate(UserData.getInstance().tourGroupId);
        onGroupStarsUpdate(UserData.getInstance().tourGroupId);
        getTourGroup();
    }

    public void onGroupUpdate(String id){
        groupListener = DBService.getInstance().OnTourGroupUpdate(id, new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                tourGroup = (TourGroup) object;


                if(UserData.getInstance().userType == userType.tourist && tourGroup != null && guidePhoto != null){
                    LatLng meLatLng = new LatLng(tourGroup.getTourGuideLocation().getLatitude(), tourGroup.getTourGuideLocation().getLongitude());
                    if(guideMarker != null)
                        guideMarker.remove();
                    guideMarker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(meLatLng)
                                    .title("Kris")
                                    .snippet("It me")
                                    .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(guidePhoto,  100, 100, false))));
                }
            }
        });
    }

    public void onGroupStarsUpdate(String id){

        DocumentReference documentReference = DBService.getInstance().GetTourGroupReference(id);

        starsListener = DBService.getInstance().OnStarsUpdate(documentReference, new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                if(!starsMarkersCurrentTour.isEmpty()){
                    for(Marker marker: starsMarkersCurrentTour){
                        marker.remove();
                    }
                }

                starsCurrentTour = (ArrayList<Star>) object;
                for(Star star: starsCurrentTour){
                    LatLng latLng = new LatLng(star.getLocation().getLatitude(), star.getLocation().getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Star");
                    markerOptions.snippet(star.getComment());
                    markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_round_star_50));
                    Marker mark = mMap.addMarker(markerOptions);
                    mark.setTag(star.getTag());
                    starsMarkersCurrentTour.add(mark);
                }
            }
        });
    }
    public  void getTourGroup(){
        DBService.getInstance().GetTourGroup(UserData.getInstance().tourGroupId, new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                tourGroup = (TourGroup) object;
                if(UserData.getInstance().userType.toString().equals("tourist"))
                    StorageService.getInstance().downloadPhoto("guide", tourGroup.getTourGuide(), "cover", new FirebaseCallback() {
                        @Override
                        public void onCallback(Object object) {
                            guidePhoto = (Bitmap) object;
                        }
                    });
            }
        });
    }
    public String getRandomNumber(){
        Random rand = new Random();
        int upperbound = 1000000;
        int int_random = rand.nextInt(upperbound);
        return String.valueOf(int_random);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}