package rs.elfak.mosis.kristijan.heavenguide.ui.friends;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.ProfileActivity;
import rs.elfak.mosis.kristijan.heavenguide.ProfileFriendsAdapter;
import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.data.model.ProfileFriendsItem;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;

public class FriendsFragment extends Fragment {

    private FriendsViewModel friendsViewModel;

    private ListView friendsListView;
    private ArrayList<User> friendsUserData;
    private ArrayList<ProfileFriendsItem> profileFriends = new ArrayList<>();
    private ProfileFriendsAdapter friendsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendsViewModel =
                ViewModelProviders.of(this).get(FriendsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_friends, container, false);
        final TextView textView = root.findViewById(R.id.text_friends);
        friendsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        fillFriendsList(root);
        //friendsUserData = UserData.getInstance().friends;

        return root;
    }

    public void fillFriendsList(final View root){
        friendsListView = root.findViewById(R.id.profile_friends_list_view);

        profileFriends.add(new ProfileFriendsItem(R.drawable.ic_menu_camera, "Brat 1"));
        profileFriends.add(new ProfileFriendsItem(R.drawable.ic_menu_gallery, "Brat 2"));
        profileFriends.add(new ProfileFriendsItem(R.drawable.ic_menu_slideshow, "Brat 3"));
        profileFriends.add(new ProfileFriendsItem(R.drawable.ic_baseline_person_24, "Brat 4"));

        friendsAdapter = new ProfileFriendsAdapter((Activity) root.getContext(), profileFriends);
        friendsListView.setAdapter(friendsAdapter);

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText((Activity) root.getContext(), "click to item: " + i, Toast.LENGTH_SHORT).show();
            }
        });
    }
}