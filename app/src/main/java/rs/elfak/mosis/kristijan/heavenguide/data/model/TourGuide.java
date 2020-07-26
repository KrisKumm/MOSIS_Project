package rs.elfak.mosis.kristijan.heavenguide.data.model;

import java.util.ArrayList;

public class TourGuide extends User {

    private String description;
    private ArrayList<String> myTours;
    private String managerID;

    public  TourGuide(){}

    public TourGuide(String name, String uId, String portrait, ArrayList<String> friends, String description, ArrayList<String> myTours, String managerID){
        super(name, uId, portrait, friends);
        this.description = description;
        this.myTours = myTours;
        this.managerID = managerID;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getMyTours() {
        return myTours;
    }

    public String getManagerID() {
        return managerID;
    }
}
