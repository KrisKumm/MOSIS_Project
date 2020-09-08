package rs.elfak.mosis.kristijan.heavenguide.data.model.items;

import android.graphics.Bitmap;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Tour;

public class ProfileTourItem {
    private Bitmap mImageResource;
    private String mRegionName;
    private String mDateTime;
    private String mGuideName;
    private Tour mTour;

    public ProfileTourItem(Bitmap imageResource, String regionName, String dateTime, String guideName, Tour Tour) {
        this.mImageResource = imageResource;
        this.mRegionName = regionName;
        this.mDateTime = dateTime;
        this.mGuideName = guideName;
        this.mTour = Tour;
    }

    public Bitmap getImageResource() {
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

    public Tour getTour() { return mTour; }
}

