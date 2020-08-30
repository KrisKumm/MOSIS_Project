package rs.elfak.mosis.kristijan.heavenguide.ui.friends;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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

import rs.elfak.mosis.kristijan.heavenguide.OtherPersonActivity;
import rs.elfak.mosis.kristijan.heavenguide.ProfileActivity;
import rs.elfak.mosis.kristijan.heavenguide.ProfileFriendsAdapter;
import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.data.model.ProfileFriendsItem;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;

public class FriendsFragment extends Fragment {

    private FriendsViewModel friendsViewModel;

    private ListView friendsListView;
    private ArrayList<User> friendsUserData = new ArrayList<User>();
    private ArrayList<Bitmap> friendPhotos = new ArrayList<Bitmap>();
    private ArrayList<ProfileFriendsItem> profileFriends = new ArrayList<>();
    private ProfileFriendsAdapter friendsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        friendsViewModel =
                ViewModelProviders.of(this).get(FriendsViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_friends, container, false);
        final TextView textView = root.findViewById(R.id.text_friends);
        friendsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        friendsListView = root.findViewById(R.id.profile_friends_list_view);

        setListClickHandler(root);
        getFriends(root);
        fillFriendsList(root);
        return root;
    }

    public void fillFriendsList(final View root){
        friendsAdapter = new ProfileFriendsAdapter((Activity) root.getContext(), profileFriends);
        friendsListView.setAdapter(friendsAdapter);
    }
    public void setListClickHandler(final View root){
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserData.getInstance().friend = friendsUserData.get(i);
                UserData.getInstance().friendPhoto = friendPhotos.get(i);
                Intent intent = new Intent( getActivity().getBaseContext() , OtherPersonActivity.class);
                startActivity(intent);
                Toast.makeText((Activity) root.getContext(), "click to item: " + i, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getFriends(final View root){
        for(String id : UserData.getInstance().friends){
            DBService.getInstance().GetUser(id, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    final User user = (User) object;
                    friendsUserData.add(user);
                    StorageService.getInstance().downloadPhoto("tourist", user.getUId(), "portrait", new FirebaseCallback() {
                        @Override
                        public void onCallback(Object object) {
                            friendPhotos.add((Bitmap) object);
                            profileFriends.add(new ProfileFriendsItem((Bitmap) object, user.getName()));
                            fillFriendsList(root);
                        }
                    });
                }
            });
        }
    }
}