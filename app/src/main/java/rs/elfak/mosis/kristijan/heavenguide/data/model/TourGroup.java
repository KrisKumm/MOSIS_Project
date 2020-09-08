package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class TourGroup {

    private String uid;
    private String tourId;
    private String tourGuide;
    private boolean readyAll;
    private GeoPoint tourGuideLocation;
    private ArrayList<String> touristIDList;
    @Exclude
    private ArrayList<Star> stars;
    // A DA TURIMO OVDE ONE ZVEZDICE NA MAPI, PA DA BUDU NA MAPI SAMO DOK TRAJE TURA??

    public  TourGroup(){}

    public TourGroup(String uId, String tourId, String tourGuide, boolean readyAll, GeoPoint tourGuideLocation, ArrayList<String> touristIDList){
        this.uid = uId;
        this.tourId = tourId;
        this.tourGuide = tourGuide;
        this.readyAll = readyAll;
        this.tourGuideLocation = tourGuideLocation;
        this.touristIDList = touristIDList;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String id){ this.uid = id;}

    public String getTourId() {
        return tourId;
    }

    public String getTourGuide() {
        return tourGuide;
    }

    public boolean isReadyAll() {
        return readyAll;
    }

    public ArrayList<String> getTouristIDList() {
        return touristIDList;
    }

    public ArrayList<Star> getStars() {
        return stars;
    }

    public GeoPoint getTourGuideLocation() {
        return tourGuideLocation;
    }
}
