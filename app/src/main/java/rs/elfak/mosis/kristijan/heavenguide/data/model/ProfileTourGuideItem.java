package rs.elfak.mosis.kristijan.heavenguide.data.model;

public class ProfileTourGuideItem {
    private String mStartEndTime;
    private String mTourName;

    public ProfileTourGuideItem(String StartEndTime, String TourName) {
        this.mStartEndTime = StartEndTime;
        this.mTourName = TourName;
    }

    public String getStartEndTime() {
        return mStartEndTime;
    }

    public String getTourName() {
        return mTourName;
    }

}
