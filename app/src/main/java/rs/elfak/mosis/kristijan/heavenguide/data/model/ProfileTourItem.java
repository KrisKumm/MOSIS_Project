package rs.elfak.mosis.kristijan.heavenguide.data.model;

public class ProfileTourItem {
    private int mImageResource;
    private String mRegionName;
    private String mDateTime;
    private String mGuideName;

    public ProfileTourItem(int imageResource, String regionName, String dateTime, String guideName) {
        this.mImageResource = imageResource;
        this.mRegionName = regionName;
        this.mDateTime = dateTime;
        this.mGuideName = guideName;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getRegionName() {
        return mRegionName;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public String getGuideName() {
        return mGuideName;
    }

}

