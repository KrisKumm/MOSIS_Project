package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Attr;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Region;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;

public class RegionActivity extends AppCompatActivity {

    private Region myRegion;
    private ArrayList<Attraction> myAttractions;

    private LinearLayout linearLayoutRegionImages;
    private TextView regionName;
    private TextView regionDescription;
    private Button regionToursButton;
    private ListView regionAttractionsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        linearLayoutRegionImages = findViewById(R.id.linear_layout_region_images);
        regionName = findViewById(R.id.region_name_label);
        regionDescription = findViewById(R.id.description_region_label);
        regionToursButton = findViewById(R.id.tour_region_button);
        regionAttractionsListView = findViewById(R.id.region_attraction_list_view);

        regionToursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        getRegion(getIntent().getExtras().getString("REGION"));
    }

    public void getRegion(final String id){
        DBService.getInstance().GetRegion(id, new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                myRegion = (Region) object;
                setRegionInfo();
            }
        });
    }

    public void setRegionInfo(){
        regionName.setText(myRegion.getName());
        regionDescription.setText((myRegion.getDescription()));

        getAttractions(myRegion.getAttractionIDList());
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
}


// Za ubacivanje vise slika u scrolling view

//    JSONArray images = receiveData.getJSONArray("images");
//    //objectImages = new String[images.length()];
//    List<String> listaObjects = new ArrayList<String>();
//    ArrayList<String> listaMenu = new ArrayList<String>();
//                                for(int i = 0; i < images.length(); i++){
//        JSONObject slika = images.getJSONObject(i);
//
//        String sajt = "https://imoutcodebullies.000webhostapp.com/Images";
//        String link = slika.getString("Link");
//        String type = slika.getString("Tip");
//        //Imageview pravis
//        if(type.equals("Object")){
//        sajt = sajt.concat("/Object/");
//        sajt = sajt.concat(link);
//        //objectImages[i] = sajt;
//        listaObjects.add(sajt);
//        }
//        else if(type.equals("Menu"))
//        {
//        sajt = sajt.concat("/Menu/");
//        sajt = sajt.concat(link);
//        listaMenu.add(sajt);
//        listaMenu1 = listaMenu;
//        }
//        }
//        LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext()
//        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        for (int i=0; i<listaObjects.size(); i++)
//        {
//        View view = layoutInflater.inflate(R.layout.object_images, objImages, false);
//        ImageView imageView = view.findViewById(R.id.ObjectImageId);
//        Picasso.get().load(listaObjects.get(i)).into(imageView);
//
//        objImages.addView(view);
//        }