package rs.elfak.mosis.kristijan.heavenguide.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.adapters.ProfileTourAdapter;
import rs.elfak.mosis.kristijan.heavenguide.adapters.ProfileTourGuideAdapter;
import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.items.ProfileTourGuideItem;
import rs.elfak.mosis.kristijan.heavenguide.data.model.items.ProfileTourItem;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;
import rs.elfak.mosis.kristijan.heavenguide.service.TourService;

public class HomeFragment extends Fragment {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PROFILE = "profile";
    public String profileP;

    private HomeViewModel homeViewModel;

    private ImageView avatar;
    private Bitmap picture;

    private TextView usernameLabel;
    private TextView roleLabel;

    private Button favTours;
    private Button favGuides;

    private ListView toursListView;
    //private ArrayList<User> tourUserData;
    private ArrayList<ProfileTourItem> profileTours = new ArrayList<>();
    private ProfileTourAdapter toursAdapter;

    private ListView toursGuideListView;
    //private ArrayList<Tour> tourGuideUserData;
    private ArrayList<ProfileTourGuideItem> profileToursGuide = new ArrayList<>();
    private ProfileTourGuideAdapter toursGuideAdatpter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        avatar = root.findViewById(R.id.imageViewAvatar);
        usernameLabel = root.findViewById(R.id.Username_label);
        roleLabel = root.findViewById(R.id.Role_label);

        StorageService.getInstance().downloadPhoto(UserData.getInstance().userType.toString(), UserData.getInstance().uId, "cover", new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                avatar.setImageBitmap(Bitmap.createScaledBitmap((Bitmap) object,  avatar.getWidth(), avatar.getHeight(),false));
            }
        });

        usernameLabel.setText(UserData.getInstance().name);
        roleLabel.setText(UserData.getInstance().userType.toString());

        favTours = root.findViewById(R.id.favorite_tours_button);
        favGuides = root.findViewById(R.id.favorite_guides_button);

        favTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        favGuides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        toursListView = root.findViewById(R.id.profile_home_tours_list_view);
        toursGuideListView = root.findViewById(R.id.profile_home_tours_guide_list_view);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, getContext().MODE_PRIVATE);
        profileP = sharedPreferences.getString(PROFILE, "");

        if(profileP.equals("tourist")){
            toursListView.setEnabled(true);
            toursListView.setVisibility(View.VISIBLE);
            toursGuideListView.setEnabled(false);
            toursGuideListView.setVisibility(View.INVISIBLE);
            fillToursList(root);
        }
        if(profileP.equals("guide")){
            toursGuideListView.setEnabled(true);
            toursGuideListView.setVisibility(View.VISIBLE);
            toursListView.setEnabled(false);
            toursListView.setVisibility(View.INVISIBLE);
            fillToursGuideList(root);
        }
        if(profileP.equals("manager")){
            toursListView.setEnabled(false);
            toursListView.setVisibility(View.INVISIBLE);
            toursGuideListView.setEnabled(false);
            toursGuideListView.setVisibility(View.INVISIBLE);
        }
        //tourUserData = UserData.getInstance().tours;

        return root;
    }

    public void fillToursList(final View root){
        toursListView = root.findViewById(R.id.profile_home_tours_list_view);

        profileTours.add(new ProfileTourItem(R.drawable.baseline_west_black_18dp, "Region 1", "Start 1", "Vodja 1"));
        profileTours.add(new ProfileTourItem(R.drawable.baseline_east_black_18dp, "Region 2", "Start 2", "Vodja 2"));
        profileTours.add(new ProfileTourItem(R.drawable.baseline_south_black_18dp, "Region 3", "Start 3", "Vodja 3"));
        profileTours.add(new ProfileTourItem(R.drawable.baseline_north_black_18dp, "Region 4", "Start 4", "Vodja 4"));

        toursAdapter = new ProfileTourAdapter((Activity) root.getContext(), profileTours);
        toursListView.setAdapter(toursAdapter);

        toursListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText((Activity) root.getContext(), "click to item: " + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fillToursGuideList(final View root){
        toursGuideListView = root.findViewById(R.id.profile_home_tours_guide_list_view);

        profileToursGuide.add(new ProfileTourGuideItem("Danas - Sutra 1 ", "Tura 1"));
        profileToursGuide.add(new ProfileTourGuideItem("Danas - Sutra 2", "Tura 2"));
        profileToursGuide.add(new ProfileTourGuideItem("Danas - Sutra 3", "Tura 3"));
        profileToursGuide.add(new ProfileTourGuideItem("Danas - Sutra 4", "Tura 4"));

        toursGuideAdatpter = new ProfileTourGuideAdapter((Activity) root.getContext(), profileToursGuide);
        toursGuideListView.setAdapter(toursGuideAdatpter);

        toursGuideListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText((Activity) root.getContext(), "click to item: " + i, Toast.LENGTH_SHORT).show();
            }
        });
    }
}