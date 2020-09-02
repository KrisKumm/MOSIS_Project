package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class TourGuide extends User {

    private String description;
    private String managerID;


    public  TourGuide(){}

    public TourGuide(String name, String uId, ArrayList<String> friends, String description, ArrayList<String> myTours, String managerID){
        super(name, uId, friends);
        this.description = description;
        this.managerID = managerID;
    }

    public String getDescription() {
        return description;
    }

    public String getManagerID() {
        return managerID;
    }
}
