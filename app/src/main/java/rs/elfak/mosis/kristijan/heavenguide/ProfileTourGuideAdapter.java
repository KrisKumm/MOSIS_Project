package rs.elfak.mosis.kristijan.heavenguide;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.model.ProfileTourGuideItem;
import rs.elfak.mosis.kristijan.heavenguide.data.model.ProfileTourItem;

public class ProfileTourGuideAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<ProfileTourGuideItem> toursGuide;
    private static LayoutInflater inflater = null;

    public ProfileTourGuideAdapter(Activity context, ArrayList<ProfileTourGuideItem> toursGuide){
        this.context = context;
        this.toursGuide = toursGuide;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return toursGuide.size();
    }

    @Override
    public ProfileTourGuideItem getItem(int i) {
        return toursGuide.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        itemView = (itemView == null) ? inflater.inflate(R.layout.profile_home_tour_guide_list_item, null): itemView;
        TextView tourGuideStart = (TextView) itemView.findViewById(R.id.profile_home_tour_guide_start_label);
        TextView tourName = (TextView) itemView.findViewById(R.id.profile_home_tour_tour_name_label);
        ProfileTourGuideItem profileTourGuideItem = toursGuide.get(i);
        tourGuideStart.setText(profileTourGuideItem.getStartEndTime());
        tourName.setText(profileTourGuideItem.getTourName());
        return itemView;
    }
}
