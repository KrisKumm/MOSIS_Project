package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Region {

    private String uid;
    private String name;
    private String description;
    private int radius;
    private GeoPoint centerPoint;
    private ArrayList<String> tours;
    private ArrayList<String> attractions;

    public Region(){}

    public Region(String uId, String name, String description, int radius, GeoPoint centerPoint, ArrayList<String> tours, ArrayList<String> attractionIDList){

        this.uid = uId;
        this.name = name;
        this.description = description;
        this.radius = radius;
        this.centerPoint = centerPoint;
        this.tours = tours;
        this.attractions = attractionIDList;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid){ this.uid = uid;}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRadius() {
        return radius;
    }

    public GeoPoint getCenterPoint() {
        return centerPoint;
    }

    public ArrayList<String> getTours() {
        return tours;
    }

    public ArrayList<String> getAttractionIDList() {
        return attractions;
    }
}
