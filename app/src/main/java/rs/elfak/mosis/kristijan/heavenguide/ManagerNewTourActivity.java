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
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;

public class ManagerNewTourActivity extends AppCompatActivity {

    private EditText newTourGuideName, newTourName, newTourDescription;
    private EditText newTourStartTime, newTourEndTime;
    private EditText newTourRegionName;
    private Button newTourAddButton;

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
        newTourStartTime = findViewById(R.id.new_tour_tour_start_time);
        newTourEndTime = findViewById(R.id.new_tour_tour_end_time);
        newTourRegionName = findViewById(R.id.new_tour_tour_region);
        newTourAddButton = findViewById(R.id.new_tour_add_button);

        newTourAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!newTourName.getText().toString().isEmpty() && !newTourDescription.getText().toString().isEmpty() && !newTourStartTime.getText().toString().isEmpty()
                && !newTourEndTime.getText().toString().isEmpty() && !newTourRegionName.getText().toString().isEmpty()) {
                    DBService.getInstance().GetGuideByName(newTourGuideName.getText().toString(), new FirebaseCallback() {
                        @Override
                        public void onCallback(Object object) {
                            TourGuide guide = (TourGuide) object;
                            if (guide != null) {
                                Tour newTour = new Tour(null, UserData.getInstance().uId, guide.getUid(), newTourName.getText().toString(), newTourDescription.getText().toString(),
                                        newTourStartTime.getText().toString(), newTourEndTime.getText().toString(), newTourRegionName.getText().toString(),
                                        new ArrayList<String>(), new ArrayList<String>());
                                String tourUid = DBService.getInstance().AddTour(newTour, UserData.getInstance().uId);
                                if(tourUid != null){
                                    UserData.getInstance().itsMeM.getTours().add(tourUid);
                                    guide.getMyTours().add(tourUid);
                                    DBService.getInstance().AddGuide(guide);
                                    StorageService.getInstance().uploadPhoto("tour", tourUid, "cover", picture, ManagerNewTourActivity.this);
                                    Toast.makeText(ManagerNewTourActivity.this, "A new tour has been created", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ManagerNewTourActivity.this, ProfileActivity.class);
                                    startActivity(i);
                                }

                            } else {
                                Toast.makeText(ManagerNewTourActivity.this, "Guide with that name doesen't exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(ManagerNewTourActivity.this, "Some field is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}