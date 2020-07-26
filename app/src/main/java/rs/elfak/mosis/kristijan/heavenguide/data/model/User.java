package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class User {

    protected String name;
    protected String uId;
    protected String portrait;
    protected ArrayList<String> friends;
    protected GeoPoint location;

    public User(){

    }

    public User(String name, String uId, String portrait, ArrayList<String> friends){
        this.name = name;
        this.uId = uId;
        this.portrait = portrait;
        this.friends = friends;
    }

    public String getName(){
        return name;
    }

    public String getUId(){
        return uId;
    }

    public String getPortrait(){
        return portrait;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
