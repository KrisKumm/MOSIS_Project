package rs.elfak.mosis.kristijan.heavenguide.data;


import android.app.Notification;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;


public class UserData {


    public String name;
    public String uId;
    public String password;
    public String gmail;
    public userType userType;
    public String portrait;
    public GeoPoint location;
    public ArrayList<String> friends;
    public ArrayList<Notification> notifications;

    private static UserData instance = null;
    private UserData()
    {

    }
    public static UserData getInstance()
    {
        if (instance == null)
            instance = new UserData();

        return instance;
    }


}
