package rs.elfak.mosis.kristijan.heavenguide.data.model.items;

import android.graphics.Bitmap;

public class SearchRecyclerItem {
    private Bitmap mImageResource;
    private String mText1;

    public SearchRecyclerItem(Bitmap imageResource, String text1) {
        mImageResource = imageResource;
        mText1 = text1;
    }

    public Bitmap getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public void changeText1(String text) {
        mText1 = text;
    }
}
