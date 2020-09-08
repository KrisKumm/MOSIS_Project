package rs.elfak.mosis.kristijan.heavenguide.ui.notifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.adapters.ProfileNotificationAdapter;
import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.data.model.items.ProfileNotificationItem;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;

public class NotificationsFragment extends Fragment {

    public View rootView;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PROFILE = "profile";
    public String profileP;

    private NotificationsViewModel notificationsViewModel;

    private ListView notificationListView;
    private ArrayList<Notification> notificationsUserData;
    private ArrayList<ProfileNotificationItem> profileNotifications = new ArrayList<>();
    private ProfileNotificationAdapter notificationsAdapter;

    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView popUpSender, popUpMessage;
    private EditText popUpReplyMessage;
    private Button popUpReplyButton, popUpOkButton, popUpSendButton, popUpDeclineButton, popUpAcceptButton;
    View root = null;

    @Override
    public void onResume() {
        notificationsUserData = UserData.getInstance().notifications;
        if(root != null)
        {
            profileNotifications.clear();
            fillNotificationsList(root);
        }
        super.onResume();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, getContext().MODE_PRIVATE);
        profileP = sharedPreferences.getString(PROFILE, "");

        notificationsUserData = UserData.getInstance().notifications;
        fillNotificationsList(root);

        return root;
    }

    public void fillNotificationsList(final View root){
        notificationListView = root.findViewById(R.id.profile_notification_list_view);

        for(Notification notification : notificationsUserData){
            profileNotifications.add(new ProfileNotificationItem(R.drawable.baseline_message_black_18dp, notification));
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
                ProfileNotificationItem clickedNotification = profileNotifications.get(i);
                if(clickedNotification.getNotification().getType() == 0){
                    createNewNotificationDialogType0(root, clickedNotification);
                }
                if(clickedNotification.getNotification().getType() == 1){
                    createNewNotificationDialogType1(root, clickedNotification);
                }

                profileNotifications.remove(i);
                notificationsAdapter = new ProfileNotificationAdapter((Activity) root.getContext(), profileNotifications);
                notificationListView.setAdapter(notificationsAdapter);
            }
        });
    }

    public void createNewNotificationDialogType0(final View root, final ProfileNotificationItem profileNotificationItem) {
        dialogBuilder = new AlertDialog.Builder(root.getContext());
        final View notificationPopUp = getLayoutInflater().inflate(R.layout.profile_notification_pop_up, null);

        popUpSender = notificationPopUp.findViewById(R.id.notification_popup_sender);
        popUpMessage = notificationPopUp.findViewById(R.id.notification_popup_message);
        popUpReplyMessage = notificationPopUp.findViewById(R.id.notification_popup_reply_message);
        popUpOkButton = notificationPopUp.findViewById(R.id.notification_popup_ok_button);
        popUpReplyButton = notificationPopUp.findViewById(R.id.notification_popup_reply_button);
        popUpSendButton = notificationPopUp.findViewById(R.id.notification_popup_send_button);

        popUpReplyMessage.setEnabled(false);
        popUpReplyMessage.setVisibility(View.INVISIBLE);
        popUpSendButton.setEnabled(false);
        popUpSendButton.setVisibility(View.INVISIBLE);

        popUpSender.setText(profileNotificationItem.getNotification().getFrom());
        popUpMessage.setText(profileNotificationItem.getNotification().getMessage());

        popUpOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBService.getInstance().DeleteNotification(DBService.getInstance().GetUserReference(UserData.getInstance().uId), profileNotificationItem.getNotification().getUid());
                dialog.dismiss();
            }
        });

        popUpReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpReplyMessage.setEnabled(true);
                popUpReplyMessage.setVisibility(View.VISIBLE);
                popUpSendButton.setEnabled(true);
                popUpSendButton.setVisibility(View.VISIBLE);
            }
        });

        popUpSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification newNotification = new Notification(UserData.getInstance().uId, popUpReplyMessage.getText().toString(), UserData.getInstance().name, DBService.getInstance().GetUserReference(UserData.getInstance().uId), 0);
                DBService.getInstance().AddNotification(DBService.getInstance().GetUserReference(profileNotificationItem.getNotification().getSender().getId()), newNotification);
                DBService.getInstance().DeleteNotification(DBService.getInstance().GetUserReference(UserData.getInstance().uId), profileNotificationItem.getNotification().getUid());
                dialog.dismiss();
            }
        });

        dialogBuilder.setView(notificationPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void createNewNotificationDialogType1(final View root, final ProfileNotificationItem profileNotificationItem) {
        dialogBuilder = new AlertDialog.Builder(root.getContext());
        final View notificationPopUp = getLayoutInflater().inflate(R.layout.popup_friend_request, null);

        popUpSender = notificationPopUp.findViewById(R.id.notification_popup_sender);
        popUpMessage = notificationPopUp.findViewById(R.id.notification_popup_message);
        popUpDeclineButton = notificationPopUp.findViewById(R.id.notification_popup_decline_button);
        popUpAcceptButton = notificationPopUp.findViewById(R.id.notification_popup_accept_button);

        popUpSender.setText(profileNotificationItem.getNotification().getFrom());
        popUpMessage.setText(profileNotificationItem.getNotification().getMessage());

        popUpDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBService.getInstance().DeleteNotification(DBService.getInstance().GetUserReference(UserData.getInstance().uId), profileNotificationItem.getNotification().getUid());
                dialog.dismiss();
            }
        });

        popUpAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData.getInstance().itsMeT.getFriends().add(profileNotificationItem.getNotification().getSender().getId());
                DBService.getInstance().AddUser(UserData.getInstance().itsMeT);

                DBService.getInstance().GetUser(profileNotificationItem.getNotification().getSender().getId(), new FirebaseCallback() {
                    @Override
                    public void onCallback(Object object) {
                        User user = (User) object;
                        user.getFriends().add(UserData.getInstance().uId);
                        DBService.getInstance().AddUser(user);
                    }
                });
                DBService.getInstance().DeleteNotification(DBService.getInstance().GetUserReference(UserData.getInstance().uId), profileNotificationItem.getNotification().getUid());

                dialog.dismiss();
            }
        });

        dialogBuilder.setView(notificationPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
    }
}