package rs.elfak.mosis.kristijan.heavenguide.data.model;

public class Review {

    private String uId;
    private int starRating;
    private String comment;

    public  Review(){}

    public Review(String uId, int starRating, String comment){
        this.uId = uId;

        this.starRating = starRating;
        this.comment = comment;
    }

    public String getuId() {
        return uId;
    }

    public int getStarRating() {
        return starRating;
    }

    public String getComment() {
        return comment;
    }
}
