package rs.elfak.mosis.kristijan.heavenguide.data;


import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;


public class UserData {


    public String name;
    public String uId;
    public String password;
    public String gmail;
    public userType userType;
    public String portrait;
    public GeoPoint location;
    public ArrayList<User> friends;
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
