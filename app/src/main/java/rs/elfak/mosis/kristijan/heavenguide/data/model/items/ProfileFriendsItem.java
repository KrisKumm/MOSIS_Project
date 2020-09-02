package rs.elfak.mosis.kristijan.heavenguide.data.model.items;

import android.graphics.Bitmap;

public class ProfileFriendsItem {
    private Bitmap mImageResource;
    private String mText1;

    public ProfileFriendsItem(Bitmap imageResource, String text1) {
        mImageResource = imageResource;
        mText1 = text1;
    }

    public Bitmap getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }
}
