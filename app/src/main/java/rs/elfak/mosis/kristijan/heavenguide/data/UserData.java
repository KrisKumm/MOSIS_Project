package rs.elfak.mosis.kristijan.heavenguide.data;


import android.graphics.Bitmap;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Manager;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Tour;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;


public class UserData {


    public String name;
    public String uId;
    public String password;
    public String gmail;
    public userType userType;
    public Bitmap portrait;
    public GeoPoint location;
    public ArrayList<String> friends;
    public ArrayList<Notification> notifications;
    public ArrayList<String> myTours;
    public User friend;
    public Bitmap friendPhoto;
    public Attraction attraction = null;
    public Bitmap attractionPhoto;
    public Tour tour;
    public Bitmap tourPhoto;
    public String tourGroupId;

    public User itsMeT;
    public TourGuide itsMeG;
    public Manager itsMeM;


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
