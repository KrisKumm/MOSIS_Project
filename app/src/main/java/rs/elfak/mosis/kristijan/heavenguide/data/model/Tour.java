package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class Tour{

    private String uid;
    private String managerId;
    private String guideId;
    private String name;
    private String description;
    private String portrait;
    private GeoPoint location;
    private Timestamp startsAt;
    private Timestamp endsAt;
    private String myRegion;
    private ArrayList<String> tourGuides;
    private ArrayList<String> attractions;
    private ArrayList<String> pendingTourists;
    public ArrayList<Review> reviews;

    Tour(){}

    Tour(String uId, String managerId, String guideId, String name, String description, String portrait, GeoPoint location,
         Timestamp startsAt, Timestamp endsAt, String myRegion, ArrayList<String> tourGuides, ArrayList<String> attractions,
         ArrayList<String> pendingTourists){
        this.uid = uId;
        this.managerId = managerId;
        this.guideId = guideId;
        this.name = name;
        this.description = description;
        this.portrait = portrait;
        this.location = location;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.myRegion = myRegion;
        this.tourGuides = tourGuides;
        this.attractions = attractions;
        this.pendingTourists = pendingTourists;
    }

    public String getUId(){
        return this.uid;
    }

    public void setUid(String id){ this.uid = id;}

    public String getManagerId(){
        return this.managerId;
    }

    public String getGuideId(){
        return this.guideId;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public String getPortrait(){
        return this.portrait;
    }

    public GeoPoint getLocation(){
        return this.location;
    }

    public Timestamp getStartsAt(){
        return this.startsAt;
    }

    public Timestamp getEndsAt(){
        return this.endsAt;
    }

    public String getMyRegion(){
        return this.myRegion;
    }

    public ArrayList<String> getAttractions(){
        return this.attractions;
    }

    public ArrayList<String> getPendingTourists(){
        return this.pendingTourists;
    }

    public ArrayList<String> getTourGuides() {
        return tourGuides;
    }

    public ArrayList<Review> getReviews() {return reviews;}
}
