package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Manager{

    private String uId;
    private String name;
    private String portrait;
    private ArrayList<String> tourGuides;
    private ArrayList<String> attractions;
    private ArrayList<String> tours;
    @Exclude
    public ArrayList<Notification> notifications;


    Manager(){}

    Manager(String uId, String name, String portrait, ArrayList<String> tourGuides , ArrayList<String> attractions, ArrayList<String> tours){
        this.uId = uId;
        this.name = name;
        this.portrait = portrait;
        this.tourGuides = tourGuides;
        this.attractions = attractions;
        this.tours = tours;
    }

    public String getUid(){
        return this.uId;
    }
    public String getName(){
        return this.name;
    }
    public String getPortrait(){
        return this.portrait;
    }
    public ArrayList<String> getTourGuides() {
        return tourGuides;
    }
    public ArrayList<String> getAtractions(){
        return this.attractions;
    }
    public ArrayList<String> getTours(){
        return this.tours;
    }
}
