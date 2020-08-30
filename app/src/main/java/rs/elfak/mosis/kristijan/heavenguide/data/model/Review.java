package rs.elfak.mosis.kristijan.heavenguide.data.model;

public class Review {

    private String uid;
    private int starRating;
    private String comment;

    public  Review(){}

    public Review(String uId, int starRating, String comment){
        this.uid = uId;

        this.starRating = starRating;
        this.comment = comment;
    }

    public String getuId() {return uid;}

    public void setUid(String id){ this.uid = id;}

    public int getStarRating() {return starRating;}

    public String getComment() {return comment;}
}
