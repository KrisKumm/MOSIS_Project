package rs.elfak.mosis.kristijan.heavenguide.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.model.items.ProfileTourItem;

public class ProfileTourAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<ProfileTourItem> tours;
    private static LayoutInflater inflater = null;

    public ProfileTourAdapter(Activity context, ArrayList<ProfileTourItem> tours){
        this.context = context;
        this.tours = tours;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tours.size();
    }

    @Override
    public ProfileTourItem getItem(int i) {
        return tours.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        itemView = (itemView == null) ? inflater.inflate(R.layout.profile_home_tour_list_item, null): itemView;
        ImageView imageView = (ImageView) itemView.findViewById(R.id.profile_home_tours_imageView);
        TextView guideName = (TextView) itemView.findViewById(R.id.profile_home_tours_guide_name_label);
        TextView regionName = (TextView) itemView.findViewById(R.id.profile_home_tours_region_name_label);
        TextView startTime = (TextView) itemView.findViewById(R.id.profile_home_tours_start_time_label);
        ProfileTourItem profileTourItem = tours.get(i);
        imageView.setImageResource(profileTourItem.getImageResource());
        guideName.setText(profileTourItem.getGuideName());
        regionName.setText(profileTourItem.getRegionName());
        startTime.setText(profileTourItem.getDateTime());
        return itemView;
    }
}
