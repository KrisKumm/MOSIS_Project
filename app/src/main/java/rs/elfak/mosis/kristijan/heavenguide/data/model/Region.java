package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Region {

    private String uId;
    private String name;
    private String description;
    private int radius;
    private GeoPoint centerPoint;
    private ArrayList<String> tours;
    private ArrayList<String> attractionIDList;

    public  Region(){}

    public Region(String uId, String name, String description, int radius, GeoPoint centerPoint, ArrayList<String> tours, ArrayList<String> attractionIDList){

        this.uId = uId;
        this.name = name;
        this.description = description;
        this.radius = radius;
        this.centerPoint = centerPoint;
        this.tours = tours;
        this.attractionIDList = attractionIDList;
    }

    public String getUId() {
        return uId;
    }

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
        return attractionIDList;
    }
}
