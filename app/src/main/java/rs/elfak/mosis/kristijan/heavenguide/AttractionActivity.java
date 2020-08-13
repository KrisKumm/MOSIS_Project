package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.SearchRecyclerItem;

public class AttractionActivity extends AppCompatActivity {

    private Attraction myAttraction;

    private ImageView attractionImageView;
    private Bitmap picture;
    private TextView attractionName;
    private TextView attractionRegionName;
    private Button editManagerButton;
    private TextView ratingLabel;
    private RatingBar ratingBar;
    private TextView ratingBarGradeLabel;
    private TextView attractionDescription;
    private ListView reviewList;

    //ReviewListAdapter & ReviewListItem & ReviewListItemArray
//    private RecyclerViewAdapter mAdapter;
//    private ArrayList<SearchRecyclerItem> searchRecyclerItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction);

        attractionImageView = findViewById(R.id.imageViewAttraction);
        attractionName = findViewById(R.id.attraction_name_label);
        attractionRegionName = findViewById(R.id.attraction_region_label);
        editManagerButton = findViewById(R.id.edit_attraction_button);
        ratingLabel = findViewById(R.id.rating_bar_label);
        ratingBar = findViewById(R.id.rating_bar_attraction);
        ratingBarGradeLabel = findViewById(R.id.rating_bar_grade_label);
        attractionDescription = findViewById(R.id.description_attraction_label);
        reviewList = findViewById(R.id.attraction_reviews_list);

        editManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        getAttraction();
        setAttractionInfo();
    }

    private void getAttraction(){

    }

    private void setAttractionInfo(){
        
    }
}