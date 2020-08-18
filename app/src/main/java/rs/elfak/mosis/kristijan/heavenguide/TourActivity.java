package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Attr;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Review;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Tour;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;

public class TourActivity extends AppCompatActivity {

    private Tour myTour;
    private TourGuide myGuide;
    private ArrayList<Attraction> myAttractions = new ArrayList<Attraction>();

    private LinearLayout linearLayoutTourImages;
    private TextView tourName;
    private TextView tourGuideName;
    private TextView tourStartEndTime;
    private TextView tourRatingGrade;
    private TextView tourDescription;
    private ListView tourTouristsListView;
    private ListView tourAttractionsListView;
    private Button tourStartButton;
    private Button tourDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        linearLayoutTourImages = findViewById(R.id.linear_layout_tour_images);
        tourName = findViewById(R.id.tour_name_label);
        tourGuideName = findViewById(R.id.tour_guide_label);
        tourStartEndTime = findViewById(R.id.tour_start_end_label);
        tourRatingGrade = findViewById(R.id.tour_rating_grade_label);
        tourDescription = findViewById(R.id.description_tour_label);
        tourTouristsListView = findViewById(R.id.tour_tourists_list_view);
        tourAttractionsListView = findViewById(R.id.tour_attractions_list_view);
        tourStartButton = findViewById(R.id.tour_start_button);
        tourDeleteButton = findViewById(R.id.tour_delete_button);

        tourStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tourDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        getTour(getIntent().getExtras().getString("TOUR"));
    }

    public void getTour(final String id){
        DBService.getInstance().GetAttraction(id, new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                Tour tour = (Tour) object;
                myTour = tour;
                setTourInfo();
                userTypeCheck();
            }
        });
    }

    public void setTourInfo(){
        tourName.setText(myTour.getName());
        tourDescription.setText((myTour.getDescription()));
        tourStartEndTime.setText(myTour.getStartsAt().toString() + " - " + myTour.getEndsAt().toString());

        getGuide(myTour.getGuideId());
        getAttractions(myTour.getAttractions());
        setReviewInfo(myTour.getReviews());
    }

    private void userTypeCheck(){
        //TODO ukloni ili dodaj dugmice za managera ili vodica
    }

    private void getGuide(final String id){
        DBService.getInstance().GetGuide(id, new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                myGuide = (TourGuide) object;
                setGuideInfo();
            }
        });

    }
    private void setGuideInfo(){
        tourGuideName.setText(myGuide.getName());
    }

    private void getAttractions(ArrayList<String> attractionIds){
        for (String id : attractionIds){
            DBService.getInstance().GetAttraction(id, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    Attraction attraction  = (Attraction) object;
                    myAttractions.add(attraction);
                    setAttractionInfo(attraction);
                }
            });
        }
    }
    private void setAttractionInfo(Attraction attraction){
        //TODO nzm kako radi ovaj ListView
    }

    private void setReviewInfo(ArrayList<Review> reviews){
        //TODO jos nismo odlucili kako ce da racunamo ocenu za bilo sta
    }
}