package rs.elfak.mosis.kristijan.heavenguide.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.model.items.ProfileFriendsItem;

public class ProfileFriendsAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<ProfileFriendsItem> friends;
    private static LayoutInflater inflater = null;

    public ProfileFriendsAdapter(Activity context, ArrayList<ProfileFriendsItem> friends){
        this.context = context;
        this.friends = friends;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public ProfileFriendsItem getItem(int i) {
        return friends.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        itemView = (itemView == null) ? inflater.inflate(R.layout.profile_friends_list_item, null): itemView;
        ImageView imageView = (ImageView) itemView.findViewById(R.id.profile_friends_list_imageView);
        TextView textView = (TextView) itemView.findViewById(R.id.profile_friends_list_textView);
        ProfileFriendsItem profileFriendsItem = friends.get(i);
        Bitmap image = profileFriendsItem.getImageResource();
        imageView.setImageBitmap(Bitmap.createScaledBitmap(image,80, 80,false));
        textView.setText(profileFriendsItem.getText1());
        return itemView;
    }
}
