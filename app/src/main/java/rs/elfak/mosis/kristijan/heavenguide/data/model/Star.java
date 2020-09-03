package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.firestore.GeoPoint;

public class Star {



    private String uid;
    private String comment;
    private String picture;
    private GeoPoint location;
    private String tag;

    public Star(){}

    public Star(String uid, String comment, String picture, GeoPoint location, String tag){
        this.uid = uid;
        this.comment = comment;
        this.picture = picture;
        this.location = location;
        this.tag = tag;
    }


    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {this.uid = uid;}

    public String getComment() {
        return comment;
    }

    public String getPicture() {
        return picture;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getTag() { return tag; }
}
