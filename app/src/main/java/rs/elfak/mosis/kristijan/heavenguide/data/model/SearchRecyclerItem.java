package rs.elfak.mosis.kristijan.heavenguide.data.model;

import android.graphics.Bitmap;

public class SearchRecyclerItem {
    private Bitmap mImageResource;
    private String mText1;
    private String mText2;

    public SearchRecyclerItem(Bitmap imageResource, String text1, String text2) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    public Bitmap getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

    public void changeText1(String text) {
        mText1 = text;
    }
}
