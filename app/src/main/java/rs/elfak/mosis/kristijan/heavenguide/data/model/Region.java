package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Region {

    private String uID;
    private String name;
    private String description;
    private List<String> imageIDList;
    private List<String> tourIDList;
    private LatLng centerPoint;
    private int radius;
    private List<String> attractionIDList;

    public Region(){

    }
}
