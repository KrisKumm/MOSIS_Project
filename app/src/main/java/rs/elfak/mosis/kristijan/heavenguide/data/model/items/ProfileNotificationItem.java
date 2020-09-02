package rs.elfak.mosis.kristijan.heavenguide.data.model.items;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;

public class ProfileNotificationItem {
    private int mImageResource;
    private String mText1;
    private Notification mNotification;

    public ProfileNotificationItem(int imageResource, Notification mNotification1) {
        mImageResource = imageResource;
        mText1 = mNotification1.getMessage();
        mNotification = mNotification1;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public Notification getNotification() { return mNotification; }
}
