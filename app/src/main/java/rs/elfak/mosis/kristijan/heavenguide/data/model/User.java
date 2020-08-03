package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class User {

    protected String name;
    protected String uid;
    protected String portrait;
    protected ArrayList<String> friends;
    protected GeoPoint location;
    public ArrayList<Notification> notifications;

    public User(){

    }

    public User(String name, String uid, ArrayList<String> friends){
        this.name = name;
        this.uid = uid;
        this.friends = friends;
    }
    public User(User user){
        this.name = user.name;
        this.uid = user.uid;
        this.friends = user.friends;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUId(){
        return uid;
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
