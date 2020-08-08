package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Tour;

public class TourActivity extends AppCompatActivity {

    private Tour myTour;

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

        getTour();
        setTourInfo();
    }

    public void getTour(){

    }

    public void setTourInfo(){

    }

}