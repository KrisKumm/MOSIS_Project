package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Tour{

    private String uid;
    private String managerId;
    private String guideId;
    private String name;
    private String description;
    //private String portrait;
    //private GeoPoint location;
    private String startsAt;
    private String endsAt;
    private String myRegion;
    //private ArrayList<String> tourGuides;
    private ArrayList<String> attractions;
    private ArrayList<String> pendingTourists;
    @Exclude
    public ArrayList<Review> reviews;

    public Tour(){}

    public Tour(String uId, String managerId, String guideId, String name, String description,String startsAt, String endsAt,
                String myRegion, ArrayList<String> attractions, ArrayList<String> pendingTourists){
        this.uid = uId;
        this.managerId = managerId;
        this.guideId = guideId;
        this.name = name;
        this.description = description;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.myRegion = myRegion;
        this.attractions = attractions;
        this.pendingTourists = pendingTourists;
    }

    public String getUid(){
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


    public String getStartsAt(){
        return this.startsAt;
    }

    public String getEndsAt(){
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


    public ArrayList<Review> getReviews() {return reviews;}
}
