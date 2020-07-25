package rs.elfak.mosis.kristijan.heavenguide.data.model;

import java.sql.Timestamp;
import java.util.List;

public class Tour {

    private String uID;
    private String tourID;
    private String description;
    private String managerID;
    private String regionID;
    private List<String> tourGuideIDList;
    private List<String> attractionIDList;
    private List<String> imageIDList;
    private String road;
    private List<String> reviewIDList;
    private Timestamp startTime;
    private Timestamp endTime;
    private List<String> touristPendingIDList;

    public Tour(){

    }
}
