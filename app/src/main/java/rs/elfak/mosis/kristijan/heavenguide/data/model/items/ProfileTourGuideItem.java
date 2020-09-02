package rs.elfak.mosis.kristijan.heavenguide.data.model.items;

import android.widget.Button;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Tour;

public class ProfileTourGuideItem {
    private String mStartEndTime;
    private String mTourName;
    private Tour mTour;

    public ProfileTourGuideItem(String StartEndTime, String TourName, Tour Tour) {
        this.mStartEndTime = StartEndTime;
        this.mTourName = TourName;
        this.mTour = Tour;
    }

    public String getStartEndTime() {
        return mStartEndTime;
    }

    public String getTourName() {
        return mTourName;
    }

    public Tour getTour() { return mTour; }
}
