package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;

public class OtherPersonActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PROFILE = "tourist";
    public String profileP;

    private User otherUser;
    private Bitmap picture;

    private ImageView avatar;
    private TextView otherUsernameLabel;
    private TextView otherRoleLabel;
    private Button addFriendButton;
    private Button removeFriendButton;
    private Button sendNotificationButton;
    private Button deleteAccountButton;
    private TextView addFriendLabel;
    private TextView removeFriendLabel;
    private TextView sendNotificationLabel;
    private TextView deleteAccountLabel;

    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView popUpReceiver;
    private EditText popUpMessage;
    private Button popUpSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_person);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        profileP = sharedPreferences.getString(PROFILE, "");

        avatar = findViewById(R.id.imageViewAvatarOther);
        otherUsernameLabel = findViewById(R.id.other_username_label);
        otherRoleLabel = findViewById(R.id.other_role_label);
        addFriendButton = findViewById(R.id.add_friend_button);
        addFriendLabel = findViewById(R.id.add_friend_label);
        removeFriendButton = findViewById(R.id.remove_friend_button);
        removeFriendLabel = findViewById(R.id.remove_friend_label);
        sendNotificationButton = findViewById(R.id.send_notification_button);
        sendNotificationLabel = findViewById(R.id.send_notification_label);
        deleteAccountButton = findViewById(R.id.delete_account_button);
        deleteAccountLabel = findViewById(R.id.delete_account_label);

        getUser();
        setOtherUserInfo();

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = UserData.getInstance().name + " would like to add you as their friend. Shall I connect you two? You can accept below by clicking ACCEPT";
                Notification newNotification = new Notification(otherUser.getUid(), message, UserData.getInstance().name, DBService.getInstance().GetUserReference(UserData.getInstance().uId), 1);
                DBService.getInstance().AddNotification(DBService.getInstance().GetUserReference(otherUser.getUid()), newNotification);
                Toast.makeText(OtherPersonActivity.this, "Notification sent!", Toast.LENGTH_SHORT).show();
                addFriendButton.setEnabled(false);
            }
        });

        removeFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData.getInstance().friends.remove(otherUser.getUid());
                if(profileP.equals("tourist")){
                    DBService.getInstance().AddUser(UserData.getInstance().itsMeT);
                }
                if(profileP.equals("guide")){
                    DBService.getInstance().AddUser(UserData.getInstance().itsMeG);
                }

                otherUser.getFriends().remove(UserData.getInstance().uId);
                DBService.getInstance().AddUser(otherUser);
            }
        });

        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder = new AlertDialog.Builder(OtherPersonActivity.this);
                final View sendNotificationPopUp = getLayoutInflater().inflate(R.layout.popup_other_person_send_notification, null);

                popUpReceiver = sendNotificationPopUp.findViewById(R.id.notification_popup_receiver);
                popUpMessage = sendNotificationPopUp.findViewById(R.id.notification_popup_message);
                popUpSendButton = sendNotificationPopUp.findViewById(R.id.notification_popup_send_new_button);

                popUpReceiver.setText("to: " + otherUser.getName());
                popUpMessage.setHint("Enter your message here...");
                popUpSendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Notification newNotification = new Notification(otherUser.getUid(), popUpMessage.getText().toString(), UserData.getInstance().name, DBService.getInstance().GetUserReference(UserData.getInstance().uId), 0);
                        DBService.getInstance().AddNotification(DBService.getInstance().GetUserReference(otherUser.getUid()), newNotification);
                        dialog.dismiss();
                    }
                });

                dialogBuilder.setView(sendNotificationPopUp);
                dialog = dialogBuilder.create();
                dialog.show();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBService.getInstance().DeleteUser(otherUser.getUid());
                finish();
            }
        });

        if(profileP.equals("tourist")){
            deleteAccountButton.setEnabled(false);
            deleteAccountButton.setVisibility(View.INVISIBLE);
            deleteAccountLabel.setEnabled(false);
            deleteAccountLabel.setVisibility(View.INVISIBLE);
            if(UserData.getInstance().friends.contains(otherUser.getUid())){
                addFriendButton.setEnabled(false);
                addFriendButton.setVisibility(View.INVISIBLE);
                addFriendLabel.setEnabled(false);
                addFriendLabel.setVisibility(View.INVISIBLE);
            }
        }
        if(profileP.equals("guide")){
            deleteAccountButton.setEnabled(false);
            deleteAccountButton.setVisibility(View.INVISIBLE);
            deleteAccountLabel.setEnabled(false);
            deleteAccountLabel.setVisibility(View.INVISIBLE);
            if(UserData.getInstance().friends.contains(otherUser.getUid())){
                addFriendButton.setEnabled(false);
                addFriendButton.setVisibility(View.INVISIBLE);
                addFriendLabel.setEnabled(false);
                addFriendLabel.setVisibility(View.INVISIBLE);
            }
        }
        if(profileP.equals("manager")){
            addFriendButton.setEnabled(false);
            addFriendButton.setVisibility(View.INVISIBLE);
            removeFriendButton.setEnabled(false);
            removeFriendButton.setVisibility(View.INVISIBLE);
            addFriendLabel.setEnabled(false);
            addFriendLabel.setVisibility(View.INVISIBLE);
            removeFriendLabel.setEnabled(false);
            removeFriendLabel.setVisibility(View.INVISIBLE);
        }
    }

    private void getUser(){
        otherUser = UserData.getInstance().friend;
        picture = UserData.getInstance().friendPhoto;
    }

    private void setOtherUserInfo(){
        avatar.setImageBitmap(Bitmap.createScaledBitmap(picture,  100, 100,false));
        otherUsernameLabel.setText(otherUser.getName());
    }

}