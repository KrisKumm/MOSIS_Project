package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.adapters.ProfileFriendsAdapter;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.items.ProfileFriendsItem;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Review;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Tour;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;

public class TourActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PROFILE = "profile";
    public String profileP;

    private Tour myTour;
    private TourGuide myGuide;
    private ArrayList<Attraction> myAttractions = new ArrayList<Attraction>();
    private ArrayList<Bitmap> attractionPhotos = new ArrayList<Bitmap>();
    private ArrayList<ProfileFriendsItem> atractionItems = new ArrayList<ProfileFriendsItem>();
    private ProfileFriendsAdapter attractionsAdapter;

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
    private Button tourJoinButton;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        profileP = sharedPreferences.getString(PROFILE, "");

        linearLayoutTourImages = findViewById(R.id.linear_layout_tour_images);
        tourName = findViewById(R.id.tour_name_label);
        tourGuideName = findViewById(R.id.tour_guide_label);
        tourStartEndTime = findViewById(R.id.tour_start_end_label);
        tourRatingGrade = findViewById(R.id.tour_rating_grade_label);
        tourDescription = findViewById(R.id.description_tour_label);
        tourAttractionsListView = findViewById(R.id.tour_attractions_list_view);
        tourStartButton = findViewById(R.id.tour_start_button);
        tourDeleteButton = findViewById(R.id.tour_delete_button);
        tourJoinButton = findViewById(R.id.tour_join_button);

        getTour();

        tourStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start tour
            }
        });

        tourDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBService.getInstance().DeleteTour(myTour.getUid());
            }
        });

        tourJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Join tour
            }
        });

        tourAttractionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent( (Activity) TourActivity.this, AttractionActivity.class);
                UserData.getInstance().attraction = myAttractions.get(i);
                UserData.getInstance().attractionPhoto = attractionPhotos.get(i);
                startActivity(intent);
            }
        });

        if(profileP.equals("tourist")){
            tourDeleteButton.setEnabled(false);
            tourDeleteButton.setVisibility(View.INVISIBLE);
            tourStartButton.setEnabled(false);
            tourStartButton.setVisibility(View.INVISIBLE);
        }
        if(profileP.equals("guide")){
            tourDeleteButton.setEnabled(false);
            tourDeleteButton.setVisibility(View.INVISIBLE);
            tourJoinButton.setEnabled(false);
            tourJoinButton.setVisibility(View.INVISIBLE);
        }
        if(profileP.equals("manager")){
            tourStartButton.setEnabled(false);
            tourStartButton.setVisibility(View.INVISIBLE);
            tourJoinButton.setEnabled(false);
            tourJoinButton.setVisibility(View.INVISIBLE);
        }

    }

    public void getTour(){

        if(UserData.getInstance().tour != null){
            myTour = UserData.getInstance().tour;
            setTourInfo();
        }
        else{
            String id = getIntent().getExtras().getString("TOUR");
            DBService.getInstance().GetTour(id, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    Tour tour = (Tour) object;
                    myTour = tour;
                    setTourInfo();
                }
            });
        }
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
        attractionsAdapter = new ProfileFriendsAdapter((Activity) context , atractionItems);
        tourAttractionsListView.setAdapter(attractionsAdapter);
        myAttractions.clear();
        attractionPhotos.clear();
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
    private void setAttractionInfo(final Attraction attraction){
        final LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        StorageService.getInstance().downloadPhoto("attraction", attraction.getUid(), "cover", new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                atractionItems.add(new ProfileFriendsItem((Bitmap) object, attraction.getName()));
                attractionPhotos.add((Bitmap) object);
                addImageView(layoutInflater, (Bitmap) object);
                attractionsAdapter.notifyDataSetChanged();
            }
        });
        //TODO nzm kako radi ovaj ListView
    }
    public void addImageView(LayoutInflater layoutInflater, Bitmap image){

        View view = layoutInflater.inflate(R.layout.region_images_layout, linearLayoutTourImages, false);
        ImageView imageView = view.findViewById(R.id.single_region_image_view);

        imageView.setImageBitmap(Bitmap.createScaledBitmap(image,  650, 400, false));
        linearLayoutTourImages.addView(view);
    }
    private void setReviewInfo(ArrayList<Review> reviews){
        //TODO jos nismo odlucili kako ce da racunamo ocenu za bilo sta
    }
}