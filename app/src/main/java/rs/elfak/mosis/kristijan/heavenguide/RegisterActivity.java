package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;
import rs.elfak.mosis.kristijan.heavenguide.ui.login.LoginActivity;


public class RegisterActivity extends AppCompatActivity {

    public EditText emailET, usernameET, passwordET;
    public Button imageB, registerB;
    public RadioGroup rg;
    public RadioButton touristRB, tourGuideRB;

    public ImageView avatar;
    private static final int RESULT_LOAD_IMAGE = 1; // Za gallery
    public Bitmap picture;
    public Context context;
    public int activity = 4;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.emailET);
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        imageB = findViewById(R.id.imageButton);
        avatar = findViewById(R.id.imageViewAvatar);
        registerB = findViewById(R.id.registerButton);
        rg = findViewById(R.id.radioGroup);
        touristRB = findViewById(R.id.touristRB);
        tourGuideRB = findViewById(R.id.tourGuideRB);

        imageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.createUserWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserData.getInstance().gmail = user.getEmail();
                            UserData.getInstance().uId = user.getUid();
                            if(tourGuideRB.isChecked()) {
                                UserData.getInstance().userType = userType.guide;
                                DBService.getInstance().AddGuide(new TourGuide(usernameET.getText().toString(), UserData.getInstance().uId , new ArrayList<String>(),
                                        "" , new ArrayList<String>() , "ZYoXf3MCcCaqBywc8gOXgHUQGaf1"));
                                StorageService.getInstance().uploadPhoto("guide", UserData.getInstance().uId , "cover", picture, RegisterActivity.this);
                            }
                            else {
                                DBService.getInstance().AddUser(new User(usernameET.getText().toString(), UserData.getInstance().uId , new ArrayList<String>()));
                                UserData.getInstance().userType = userType.tourist;
                                StorageService.getInstance().uploadPhoto("tourist", UserData.getInstance().uId , "cover", picture, RegisterActivity.this);
                            }
                            Toast.makeText(RegisterActivity.this, "You have been registered!", Toast.LENGTH_SHORT).show();

                            RegisterActivity.this.finish();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Register failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) //Posle edita i izbora slike
    {
        if(requestCode == 3){
            finish();
            startActivity(getIntent());
        }
        if (requestCode == activity) {
            //popuniPolja();
        }
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE  && resultCode == RESULT_OK && data != null)
        {

            Uri selectedImageUri = data.getData();
            avatar.setImageURI(selectedImageUri);
            try {
                picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);;

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}