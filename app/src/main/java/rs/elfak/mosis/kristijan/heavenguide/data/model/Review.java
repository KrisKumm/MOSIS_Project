package rs.elfak.mosis.kristijan.heavenguide.data.model;

public class Review {

    private String uid;
    private String comment;
    private String from;

    public  Review(){}

    public Review(String uId, String from, String comment){
        this.uid = uId;
        this.from = from;
        this.comment = comment;
    }

    public String getuId() {return uid;}

    public void setUid(String id){ this.uid = id;}

    public String getStarRating() {return from;}

    public String getComment() {return comment;}
}
