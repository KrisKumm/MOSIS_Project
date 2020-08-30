package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class Attraction {

    private String uid;
    private String name;
    private String description;
    private ArrayList<String> pictures;
    private GeoPoint location;
    private String myRegion;
    public ArrayList<Review> reviews;

    public Attraction(){}

    public Attraction(String uId, String name, String description, ArrayList<String> pictures, GeoPoint location, String myRegion){

        this.uid = uId;
        this.name = name;
        this.description = description;
        this.pictures = pictures;
        this.location = location;
        this.myRegion = myRegion;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String id){ this.uid = id;}

    public String getName(){return name;}

    public String getDescription(){
        return description;
    }

    public ArrayList<String> getPictures(){
        return pictures;
    }

    public GeoPoint getLocation(){
        return location;
    }

    public String getMyRegion(){
        return myRegion;
    }
}
