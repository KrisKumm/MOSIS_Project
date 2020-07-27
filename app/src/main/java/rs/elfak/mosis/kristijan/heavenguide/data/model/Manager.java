package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class Manager{

    private String uId;
    private String name;
    private String portrait;
    private ArrayList<String> tourGuides;
    private ArrayList<DocumentReference> atractions;
    private ArrayList<DocumentReference> tours;
    public ArrayList<Notification> notifications;


    Manager(){}

    Manager(String uId, String name, String portrait, ArrayList<String> tourGuides , ArrayList<DocumentReference> atractions, ArrayList<DocumentReference> tours){
        this.uId = uId;
        this.name = name;
        this.portrait = portrait;
        this.tourGuides = tourGuides;
        this.atractions = atractions;
        this.tours = tours;
    }

    public String getUId(){
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
    public ArrayList<DocumentReference> getAtractions(){
        return this.atractions;
    }
    public ArrayList<DocumentReference> getTours(){
        return this.tours;
    }
}
