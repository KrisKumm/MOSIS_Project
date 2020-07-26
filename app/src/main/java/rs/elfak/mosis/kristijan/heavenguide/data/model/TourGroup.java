package rs.elfak.mosis.kristijan.heavenguide.data.model;

import java.util.ArrayList;

public class TourGroup {

    private String uId;
    private String tourId;
    private String tourGuide;
    private boolean readyAll;
    private ArrayList<String> touristIDList;
    private ArrayList<String> starLocationIdList;

    public  TourGroup(){}

    public TourGroup(String uId, String tourId, String tourGuide, boolean readyAll, ArrayList<String> touristIDList, ArrayList<String> starLocationIdList){
        this.uId = uId;
        this.tourId = tourId;
        this.tourGuide = tourGuide;
        this.readyAll = readyAll;
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
}
