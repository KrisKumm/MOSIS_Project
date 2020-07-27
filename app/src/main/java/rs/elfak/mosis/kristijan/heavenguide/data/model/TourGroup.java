package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class TourGroup {

    private String uId;
    private String tourId;
    private String tourGuide;
    private boolean readyAll;
    private GeoPoint tourGuideLocation;
    private ArrayList<String> touristIDList;
    private ArrayList<String> starLocationIdList;
    // A DA TURIMO OVDE ONE ZVEZDICE NA MAPI, PA DA BUDU NA MAPI SAMO DOK TRAJE TURA??

    public  TourGroup(){}

    public TourGroup(String uId, String tourId, String tourGuide, boolean readyAll, GeoPoint tourGuideLocation, ArrayList<String> touristIDList, ArrayList<String> starLocationIdList){
        this.uId = uId;
        this.tourId = tourId;
        this.tourGuide = tourGuide;
        this.readyAll = readyAll;
        this.tourGuideLocation = tourGuideLocation;
        this.touristIDList = touristIDList;
        this.starLocationIdList = starLocationIdList;
    }


    public String getUId() {
        return uId;
    }

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

    public ArrayList<String> getStarLocationIdList() {
        return starLocationIdList;
    }

    public GeoPoint getTourGuideLocation() {
        return tourGuideLocation;
    }
}
