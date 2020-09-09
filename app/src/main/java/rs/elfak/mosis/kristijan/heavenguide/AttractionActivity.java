package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;

public class AttractionActivity extends AppCompatActivity {

    private Attraction myAttraction;
    private Bitmap picture;

    private ImageView attractionImageView;
    private TextView attractionName;
    private TextView attractionRegionName;
    private TextView ratingLabel;
    private RatingBar ratingBar;
    private TextView ratingBarGradeLabel;
    private TextView attractionDescription;
    private ListView reviewList;

//    ReviewListAdapter & ReviewListItem & ReviewListItemArray
//    private RecyclerViewAdapter mAdapter;
//    private ArrayList<SearchRecyclerItem> searchRecyclerItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction);

        attractionImageView = findViewById(R.id.imageViewAttraction);
        attractionName = findViewById(R.id.attraction_name_label);
        attractionRegionName = findViewById(R.id.attraction_region_label);
        ratingLabel = findViewById(R.id.rating_bar_label);
        ratingBar = findViewById(R.id.rating_bar_attraction);
        ratingBarGradeLabel = findViewById(R.id.rating_bar_grade_label);
        attractionDescription = findViewById(R.id.description_attraction_label);
        reviewList = findViewById(R.id.attraction_reviews_list);

        getAttraction();
    }

    private void getAttraction(){

        if(UserData.getInstance().attraction != null){
            myAttraction = UserData.getInstance().attraction;
            picture = UserData.getInstance().attractionPhoto;
            setAttractionInfo();
        }
        else{
            String id = getIntent().getExtras().getString("ATTRACTION");
            DBService.getInstance().GetAttraction(id, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    myAttraction = (Attraction) object;
                    picture = null;
                    setAttractionInfo();
                }
            });
        }
    }
    private void setAttractionInfo(){
        attractionName.setText(myAttraction.getName());
        attractionDescription.setText((myAttraction.getDescription()));
        attractionRegionName.setText(myAttraction.getMyRegion());
        setCoverPhoto();
        setReviewsInfo();
    }

    private void setCoverPhoto(){
        if(picture != null){
            attractionImageView.setImageBitmap(Bitmap.createScaledBitmap(picture,  200, 200,false));
        }
        else{
            StorageService.getInstance().downloadPhoto("attraction",myAttraction.getUid(), "cover", new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    attractionImageView.setImageBitmap(Bitmap.createScaledBitmap((Bitmap) object,  200, 200,false));
                }
            });
        }
    }

    private void setReviewsInfo(){
        int rating = 0, sum = 0, i = 1 ;
        for(int star: myAttraction.getStars()){
            rating += i* star;
            sum += star;
            i++;
        }
        if(sum != 0) {
            ratingBarGradeLabel.setText(rating / sum);
            ratingBar.setRating(rating / sum);
        }
        else{
            ratingBar.setRating(0);
            ratingBarGradeLabel.setText("0");
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                myAttraction.getStars().set((int)v - 1 , myAttraction.getStars().get((int) v - 1 ) + 1 );
                DBService.getInstance().AddAttraction(myAttraction, "");
                ratingBar.setEnabled(false);
            }
        });
        //TODO jos nismo odlucili kako ce da racunamo ocenu za bilo sta
    }
}