package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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


                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

//    protected void sendPicture(){ //Slika upload
//        final ImageThread ithread = new ImageThread(context, picture, loginPreferences.getString("username", ""), loginPreferences.getString("password", ""));
//        ithread.start();
//        try {
//            Thread.sleep(1500);
//            ithread.join();
//            String link = ithread.getResult();
//            if(link.equals("Error")){
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast = Toast.makeText(context, "Error uploading picture", duration);
//                toast.show();
//                //loginPrefsEditor.putString("picture", "Not Found");
//            }
//            else{
//                loginPrefsEditor.putString("picture", link);
//                loginPrefsEditor.apply();
//                Picasso.get().load(link)
//                        //.resize(100,130)
//                        .into(avatar);
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        //data.putExtra("image", ithread.getImage());
//    }

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
                picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                //sendPicture();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}