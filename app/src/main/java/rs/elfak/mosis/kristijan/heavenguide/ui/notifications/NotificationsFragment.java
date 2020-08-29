package rs.elfak.mosis.kristijan.heavenguide.ui.notifications;

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

import rs.elfak.mosis.kristijan.heavenguide.ProfileFriendsAdapter;
import rs.elfak.mosis.kristijan.heavenguide.ProfileNotificationAdapter;
import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.data.model.ProfileFriendsItem;
import rs.elfak.mosis.kristijan.heavenguide.data.model.ProfileNotificationItem;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private ListView notificationListView;
    private ArrayList<Notification> notificationsUserData;
    private ArrayList<ProfileNotificationItem> profileNotifications = new ArrayList<>();
    private ProfileNotificationAdapter notificationsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        notificationsUserData = UserData.getInstance().notifications;
        fillNotificationsList(root);

        return root;
    }

    public void fillNotificationsList(final View root){
        notificationListView = root.findViewById(R.id.profile_notification_list_view);

        for(Notification notification : notificationsUserData){
            profileNotifications.add(new ProfileNotificationItem(R.drawable.baseline_message_black_18dp, notification.getMessage()));
        }
        notificationsAdapter = new ProfileNotificationAdapter((Activity) root.getContext(), profileNotifications);
        notificationListView.setAdapter(notificationsAdapter);

        setListClickHandler(root);
    }
    public void setListClickHandler(final View root){
        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText((Activity) root.getContext(), "click to item: " + i, Toast.LENGTH_SHORT).show();
            }
        });
    }
}