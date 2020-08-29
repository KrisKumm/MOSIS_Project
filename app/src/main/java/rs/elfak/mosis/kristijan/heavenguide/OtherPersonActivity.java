package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;

public class OtherPersonActivity extends AppCompatActivity {

    private User otherUser;
    private Bitmap picture;

    private ImageView avatar;
    private TextView otherUsernameLabel;
    private TextView otherRoleLabel;
    private Button addFriendButton;
    private Button removeFriendButton;
    private Button sendNotificationButton;
    private Button addToTourButton;
    private Button deleteAccountButton;
    private TextView addFriendLabel;
    private TextView removeFriendLabel;
    private TextView sendNotificationLabel;
    private TextView addToTourLabel;
    private TextView deleteAccountLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_person);

        avatar = findViewById(R.id.imageViewAvatarOther);
        otherUsernameLabel = findViewById(R.id.other_username_label);
        otherRoleLabel = findViewById(R.id.other_role_label);
        addFriendButton = findViewById(R.id.add_friend_button);
        addFriendLabel = findViewById(R.id.add_friend_label);
        removeFriendButton = findViewById(R.id.remove_friend_button);
        removeFriendLabel = findViewById(R.id.remove_friend_label);
        sendNotificationButton = findViewById(R.id.send_notification_button);
        sendNotificationLabel = findViewById(R.id.send_notification_label);
        addToTourButton = findViewById(R.id.add_to_tour_button);
        addToTourLabel = findViewById(R.id.add_to_tour_label);
        deleteAccountButton = findViewById(R.id.delete_account_button);
        deleteAccountLabel = findViewById(R.id.delete_account_label);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        removeFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        addToTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        getUser();
        setOtherUserInfo();
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