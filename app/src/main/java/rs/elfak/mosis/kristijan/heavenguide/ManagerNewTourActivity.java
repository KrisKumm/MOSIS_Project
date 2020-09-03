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
import android.widget.Toast;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Tour;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;

public class ManagerNewTourActivity extends AppCompatActivity {

    private EditText newTourGuideName, newTourName, newTourDescription;
    private EditText newTourLatitude, newTourLongitude, newTourStartTime, newTourEndTime;
    private EditText newTourRegionName, newTourAttractionsNames;
    private Button newTourImageButton, newTourAddButton;
    private ImageView newTourImageView;

    private static final int RESULT_LOAD_IMAGE = 1; // Za gallery
    public Bitmap picture;
    public Context context;
    public int activity = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_new_tour);

        newTourGuideName = findViewById(R.id.new_tour_guide_name);
        newTourName = findViewById(R.id.new_tour_tour_name);
        newTourDescription = findViewById(R.id.new_tour_tour_description);
        newTourLatitude = findViewById(R.id.new_tour_tour_latitude);
        newTourLongitude = findViewById(R.id.new_tour_tour_longitude);
        newTourStartTime = findViewById(R.id.new_tour_tour_start_time);
        newTourEndTime = findViewById(R.id.new_tour_tour_end_time);
        newTourRegionName = findViewById(R.id.new_tour_tour_region);
        newTourAttractionsNames = findViewById(R.id.new_tour_tour_attractions);
        newTourImageButton = findViewById(R.id.new_tour_tour_image_button);
        newTourAddButton = findViewById(R.id.new_tour_add_button);
        newTourImageView = findViewById(R.id.new_tour_tour_image_view);

        newTourImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        newTourAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* TODO CHECK IF ALL THE FIELDS ARE NOT EMPTY */
                DBService.getInstance().GetGuideByName(newTourGuideName.getText().toString(), new FirebaseCallback() {
                    @Override
                    public void onCallback(Object object) {
                        TourGuide guide = (TourGuide) object;
                        if(guide != null){
                            Tour newTour = new Tour(null, UserData.getInstance().uId, guide.getUid(), newTourName.getText().toString(), newTourDescription.getText().toString(),
                                    newTourStartTime.getText().toString(), newTourEndTime.getText().toString(), newTourRegionName.getText().toString(),
                                    new ArrayList<String>(), new ArrayList<String>());
                            String tourUid = DBService.getInstance().AddTour(newTour, UserData.getInstance().uId);
                            guide.getMyTours().add(tourUid);
                            DBService.getInstance().AddGuide(guide);
                            Toast.makeText(ManagerNewTourActivity.this, "A new tour has been created", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ManagerNewTourActivity.this, ProfileActivity.class);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(ManagerNewTourActivity.this, "Guide with that name doesen't exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 3){
            finish();
            startActivity(getIntent());
        }
        if (requestCode == activity) {
            //popuniPolja();
        }
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE  && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            newTourImageView.setImageURI(selectedImageUri);
            try {
                picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}