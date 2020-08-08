package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class Attraction {

    private String uId;
    private String name;
    private String description;
    private ArrayList<String> pictures;
    private GeoPoint location;
    private DocumentReference myRegion;
    public ArrayList<Review> reviews;

    Attraction(){}

    Attraction(String uId, String name, String description, ArrayList pictures, GeoPoint location, DocumentReference myRegion){

        this.uId = uId;
        this.name = name;
        this.description = description;
        this.pictures = pictures;
        this.location = location;
        this.myRegion = myRegion;
    }

    public String getUId(){
        return uId;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public ArrayList getPictures(){
        return pictures;
    }

    public GeoPoint getLocation(){
        return location;
    }

    public DocumentReference getMyRegion(){
        return myRegion;
    }
}
