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
import rs.elfak.mosis.kristijan.heavenguide.data.model.items.ProfileNotificationItem;

public class ProfileNotificationAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<ProfileNotificationItem> notifications;
    private static LayoutInflater inflater = null;

    public ProfileNotificationAdapter(Activity context, ArrayList<ProfileNotificationItem> notifications){
        this.context = context;
        this.notifications = notifications;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public ProfileNotificationItem getItem(int i) {
        return notifications.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        itemView = (itemView == null) ? inflater.inflate(R.layout.profile_notification_list_item, null): itemView;
        ImageView imageView = (ImageView) itemView.findViewById(R.id.profile_notification_list_imageView);
        TextView textView = (TextView) itemView.findViewById(R.id.profile_notification_list_textView);
        ProfileNotificationItem profileNotificationItem = notifications.get(i);
        imageView.setImageResource(profileNotificationItem.getImageResource());
        textView.setText(profileNotificationItem.getText1());
        return itemView;
    }
}
