package rs.elfak.mosis.kristijan.heavenguide;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyAppSingleton extends Application {

    private static MyAppSingleton singleton;

    private List<Location> myLocations;

    public List<Location> getMyLocations() {
        return myLocations;
    }

    public void setMyLocations(List<Location> myLocations) {
        this.myLocations = myLocations;
    }

    public MyAppSingleton getInstance() {
        return singleton;
    }

    public void onCreate() {
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
    }
}
