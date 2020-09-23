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
import java.util.Arrays;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Tour;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;

public class ManagerNewAttractionActivity extends AppCompatActivity {

    private EditText newAttractionName, newAttractionDescription;
    private EditText newAttractionLatitude, newAttractionLongitude, newAttractionRegionName;
    private Button newAttractionImageButton, newAttractionAddButton, newAttractionGetLatLonButton;
    private ImageView newAttractionImageView;

    private static final int RESULT_LOAD_IMAGE = 1; // Za gallery
    public Bitmap picture;
    public Context context;
    public int activity = 4;

    private static final int RESULT_GET_LATLON = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_new_attraction);

        newAttractionName = findViewById(R.id.new_attraction_attraction_name);
        newAttractionDescription = findViewById(R.id.new_attraction_attraction_description);
        newAttractionLatitude = findViewById(R.id.new_attraction_attraction_latitude);
        newAttractionLongitude = findViewById(R.id.new_attraction_attraction_longitude);
        newAttractionRegionName = findViewById(R.id.new_attraction_attraction_region);
        newAttractionImageButton = findViewById(R.id.new_attraction_attraction_image_button);
        newAttractionAddButton = findViewById(R.id.new_attraction_add_button);
        newAttractionGetLatLonButton = findViewById(R.id.new_attraction_get_latlon_button);
        newAttractionImageView = findViewById(R.id.new_attraction_attraction_image_view);

        setButtons();
    }

    public void setButtons(){
        newAttractionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        newAttractionAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(picture != null && !newAttractionName.getText().toString().isEmpty() && !newAttractionDescription.getText().toString().isEmpty()
                        && !newAttractionLatitude.getText().toString().isEmpty() && !newAttractionLongitude.getText().toString().isEmpty()){
                    ArrayList stars = new ArrayList(Arrays.asList(0, 0, 0 , 0, 0));
                    Attraction newAttraction = new Attraction( null, newAttractionName.getText().toString(),newAttractionDescription.getText().toString(),new ArrayList<String>(),
                            new GeoPoint(Double.parseDouble(newAttractionLatitude.getText().toString()),Double.parseDouble(newAttractionLongitude.getText().toString())),
                            stars, newAttractionRegionName.getText().toString());
                    String uid = DBService.getInstance().AddAttraction(newAttraction, UserData.getInstance().uId);
                    if(uid != null){
                        UserData.getInstance().itsMeM.getAttractions().add(uid);
                        StorageService.getInstance().uploadPhoto("attraction", uid, "cover", picture, ManagerNewAttractionActivity.this);
                        Toast.makeText(ManagerNewAttractionActivity.this, "A new attraction has been created", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ManagerNewAttractionActivity.this, ProfileActivity.class);
                        startActivity(i);
                    }
                }
                else{
                    Toast.makeText(ManagerNewAttractionActivity.this, "Some field is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        newAttractionGetLatLonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getLatLonIntent = new Intent(ManagerNewAttractionActivity.this, MapsLatLonActivity.class);
                startActivityForResult(getLatLonIntent, RESULT_GET_LATLON);
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
            newAttractionImageView.setImageURI(selectedImageUri);
            try {
                picture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        if(requestCode == RESULT_GET_LATLON  && resultCode == RESULT_OK && data != null) {
            String lat = data.getExtras().getString("lat");
            String lon = data.getExtras().getString("lon");
            try {
                newAttractionLatitude.setText(lat);
                newAttractionLongitude.setText(lon);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}