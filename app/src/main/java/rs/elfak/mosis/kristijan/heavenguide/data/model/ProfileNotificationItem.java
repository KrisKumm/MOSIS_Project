package rs.elfak.mosis.kristijan.heavenguide.data.model;

public class ProfileNotificationItem {
    private int mImageResource;
    private String mText1;

    public ProfileNotificationItem(int imageResource, String text1) {
        mImageResource = imageResource;
        mText1 = text1;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }
}
