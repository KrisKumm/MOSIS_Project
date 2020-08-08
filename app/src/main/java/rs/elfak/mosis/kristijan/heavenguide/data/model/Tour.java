package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class Tour{

    private String uId;
    private String managerId;
    private String guideId;
    private String name;
    private String description;
    private String portrait;
    private GeoPoint location;
    private com.google.firebase.Timestamp startsAt;
    private com.google.firebase.Timestamp endsAt;
    private DocumentReference myRegion;
    private ArrayList<String> tourGuides;
    private ArrayList<DocumentReference> attractions;
    private ArrayList<String> pendingTourists;
    public ArrayList<Review> reviews;

    Tour(){}

    Tour(String uId, String managerId, String guideId, String name, String description, String portrait, GeoPoint location,
         com.google.firebase.Timestamp startsAt, com.google.firebase.Timestamp endsAt, DocumentReference myRegion, ArrayList<String> tourGuides, ArrayList<DocumentReference> attractions,
         ArrayList<String> pendingTourists){
        this.uId = uId;
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
        return this.uId;
    }
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
    public com.google.firebase.Timestamp getStartsAt(){
        return this.startsAt;
    }
    public Timestamp getEndsAt(){
        return this.endsAt;
    }
    public DocumentReference getMyRegion(){
        return this.myRegion;
    }
    public ArrayList<DocumentReference> getAttractions(){
        return this.attractions;
    }
    public ArrayList<String> getPendingTourists(){
        return this.pendingTourists;
    }
    public ArrayList<String> getTourGuides() {
        return tourGuides;
    }
}
