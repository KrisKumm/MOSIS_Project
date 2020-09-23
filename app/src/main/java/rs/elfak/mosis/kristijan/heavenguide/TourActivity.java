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
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.adapters.ProfileFriendsAdapter;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGroup;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.data.model.items.ProfileFriendsItem;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Review;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Tour;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;
import rs.elfak.mosis.kristijan.heavenguide.service.TourService;

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
    private Button tourEndButton;
    private Button tourAddAttractionButton;
    private Switch tourAttractionsSwitch;

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
        tourEndButton = findViewById(R.id.tour_end_button);
        tourAddAttractionButton = findViewById(R.id.tour_add_attraction_button);
        tourAttractionsSwitch = findViewById(R.id.tour_attractions_switch);

        getTour();

        tourStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myGuide.getMyTourGroup() == null){
                    TourGroup tourGroup = new TourGroup(null, myTour.getUid(), myTour.getGuideId(), true, new GeoPoint(0,0), new ArrayList<String>());
                    String uidGrupe = DBService.getInstance().AddTourGroup(tourGroup);

                    myGuide.setMyTourGroup(uidGrupe);
                    DBService.getInstance().AddGuide(myGuide);

                    for(String id: myTour.getPendingTourists()){
                        DBService.getInstance().UpdateUserTourGroup(id, uidGrupe);
                    }
                }
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
                boolean nesamPrijevljen = true;
                for (String ids : myTour.getPendingTourists()){
                    if(ids == UserData.getInstance().uId)
                        nesamPrijevljen = false;
                }
                if(nesamPrijevljen){
                    myTour.getPendingTourists().add(UserData.getInstance().uId);
                    DBService.getInstance().AddTour(myTour, "");
                    tourJoinButton.setEnabled(false);
                }
            }
        });

        tourEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myGuide.getMyTourGroup() != null){
                    DBService.getInstance().DeleteStars(DBService.getInstance().GetTourGroupReference(myGuide.getMyTourGroup()),myGuide.getMyTourGroup());
                    DBService.getInstance().DeleteTourGroup(myGuide.getMyTourGroup());
                    myGuide.setMyTourGroup(null);
                    DBService.getInstance().AddGuide(myGuide);
                    for(String id: myTour.getPendingTourists()){
                        DBService.getInstance().GetUser(id, new FirebaseCallback() {
                            @Override
                            public void onCallback(Object object) {
                                User user = (User) object;
                                user.setMyTourGroup(null);
                                DBService.getInstance().AddUser(user);
                            }
                        });
                    }
                    myTour.getPendingTourists().clear();
                    DBService.getInstance().AddTour(myTour, "");

                    Intent service = new Intent(TourActivity.this, TourService.class);
                    service.putExtra("TOUR_GROUP", myGuide.getMyTourGroup());
                    service.putExtra("MY_UID", UserData.getInstance().uId);
                    service.putExtra("USER_TYPE", UserData.getInstance().userType.toString());
                    startService(service);
                }
            }
        });

        tourAddAttractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!myTour.getAttractions().contains(UserData.getInstance().attraction.getUid())){
                    myTour.getAttractions().add(UserData.getInstance().attraction.getUid());
                    DBService.getInstance().AddTour(myTour, UserData.getInstance().uId);
                }
            }
        });

        tourAttractionsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutTourImages.removeAllViews();
                atractionItems.clear();
                attractionsAdapter.notifyDataSetChanged();
                if(!tourAttractionsSwitch.isChecked()){
                    getAttractions(myTour.getAttractions());
                }else{
                    ArrayList<String> filteredArray = new ArrayList<>(UserData.getInstance().itsMeM.getAttractions());
                    filteredArray.removeAll(myTour.getAttractions());
                    getAttractions(filteredArray);
                }
            }
        });

        tourAttractionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserData.getInstance().attraction = myAttractions.get(i);
                UserData.getInstance().attractionPhoto = attractionPhotos.get(i);
                if(!profileP.equals("manager") || !tourAttractionsSwitch.isChecked()) {
                    Intent intent = new Intent((Activity) TourActivity.this, AttractionActivity.class);
                    startActivity(intent);
                }
            }
        });

        if(profileP.equals("tourist")){
            tourDeleteButton.setEnabled(false);
            tourDeleteButton.setVisibility(View.INVISIBLE);
            tourStartButton.setEnabled(false);
            tourStartButton.setVisibility(View.INVISIBLE);
            tourEndButton.setEnabled(false);
            tourEndButton.setVisibility(View.INVISIBLE);
            tourAddAttractionButton.setEnabled(false);
            tourAddAttractionButton.setVisibility(View.INVISIBLE);
            tourAttractionsSwitch.setEnabled(false);
            tourAttractionsSwitch.setVisibility(View.INVISIBLE);
        }
        if(profileP.equals("guide")){
            tourDeleteButton.setEnabled(false);
            tourDeleteButton.setVisibility(View.INVISIBLE);
            tourJoinButton.setEnabled(false);
            tourJoinButton.setVisibility(View.INVISIBLE);
            tourAddAttractionButton.setEnabled(false);
            tourAddAttractionButton.setVisibility(View.INVISIBLE);
            tourAttractionsSwitch.setEnabled(false);
            tourAttractionsSwitch.setVisibility(View.INVISIBLE);
        }
        if(profileP.equals("manager")){
            tourStartButton.setEnabled(false);
            tourStartButton.setVisibility(View.INVISIBLE);
            tourJoinButton.setEnabled(false);
            tourJoinButton.setVisibility(View.INVISIBLE);
            tourEndButton.setEnabled(false);
            tourEndButton.setVisibility(View.INVISIBLE);
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
        atractionItems.clear();
        attractionsAdapter = new ProfileFriendsAdapter((Activity) context , atractionItems);
        tourAttractionsListView.setAdapter(attractionsAdapter);
        myAttractions.clear();
        attractionPhotos.clear();
        for (String id : attractionIds){
            DBService.getInstance().GetAttraction(id, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    Attraction attraction  = (Attraction) object;
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
                myAttractions.add(attraction);
                attractionPhotos.add((Bitmap) object);
                addImageView(layoutInflater, (Bitmap) object);
                attractionsAdapter.notifyDataSetChanged();
            }
        });

    }
    public void addImageView(LayoutInflater layoutInflater, Bitmap image){

        View view = layoutInflater.inflate(R.layout.region_images_layout, linearLayoutTourImages, false);
        ImageView imageView = view.findViewById(R.id.single_region_image_view);

        //imageView.setImageBitmap(Bitmap.createScaledBitmap(image,  650, 400, false));
        imageView.setImageBitmap(image);
        linearLayoutTourImages.addView(view);
    }
    private void setReviewInfo(ArrayList<Review> reviews){
        //TODO jos nismo odlucili kako ce da racunamo ocenu za bilo sta
    }
}