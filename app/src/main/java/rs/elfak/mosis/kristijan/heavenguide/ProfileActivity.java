package rs.elfak.mosis.kristijan.heavenguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.MenuItemCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Manager;
import rs.elfak.mosis.kristijan.heavenguide.data.model.SearchRecyclerItem;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;
import rs.elfak.mosis.kristijan.heavenguide.ui.login.LoginActivity;
import rs.elfak.mosis.kristijan.heavenguide.MyAppSingleton;

public class ProfileActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PROFILE = "tourist";
    public String profileP;

    private AppBarConfiguration mAppBarConfiguration;

    private RelativeLayout relativeLayoutSearch;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<SearchRecyclerItem> searchRecyclerItemArrayList;

    private ArrayList<Attraction> attractions = new ArrayList<>();
    private ArrayList<Bitmap> attractionPictures = new ArrayList<>();

    private Button buttonInsert;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setEnabled(false);
        fab.setVisibility(View.INVISIBLE);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_friends, R.id.nav_notifications)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().findItem(R.id.nav_logout_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                drawer.closeDrawers();
                finish();
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
                return true;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        profileP = sharedPreferences.getString(PROFILE, "");

        relativeLayoutSearch = findViewById(R.id.relativeLayoutSearch);
        createSearchItemList();
        buildRecyclerView();
        setButtons();
        relativeLayoutSearch.setEnabled(false);
        relativeLayoutSearch.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                relativeLayoutSearch.setEnabled(false);
//                relativeLayoutSearch.setVisibility(View.INVISIBLE);
//                return true;
//            }
//        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    relativeLayoutSearch.setEnabled(true);
                    relativeLayoutSearch.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    relativeLayoutSearch.setEnabled(false);
                    relativeLayoutSearch.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                DBService.getInstance().GetAttractionsByName(s, new FirebaseCallback() {
                    @Override
                    public void onCallback(Object object) {
                        attractions = (ArrayList<Attraction>) object;
                        for(final Attraction attraction : attractions){
                            StorageService.getInstance().downloadPhoto("attraction", attraction.getUid(), "cover", new FirebaseCallback() {
                                @Override
                                public void onCallback(Object object) {
                                    insertItem(attractionPictures.size(), (Bitmap) object, attraction.getName());
                                    attractionPictures.add((Bitmap) object);
                                }
                            });
                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
               // Toast.makeText( ProfileActivity.this , s , Toast.LENGTH_LONG);
                // mAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_map:
                Intent i1 = new Intent(ProfileActivity.this, MapsActivity.class);
                startActivity(i1);
                return true;
            case R.id.action_search:
//                if(!relativeLayoutSearch.isEnabled()){
//                    relativeLayoutSearch.setEnabled(true);
//                    relativeLayoutSearch.setVisibility(View.VISIBLE);
//                }
//                else if(relativeLayoutSearch.isEnabled()){
//                    relativeLayoutSearch.setEnabled(false);
//                    relativeLayoutSearch.setVisibility(View.INVISIBLE);
//                }

                return true;
            case R.id.action_settings:
                Intent i2 = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void createSearchItemList(){
        searchRecyclerItemArrayList = new ArrayList<>();

//        searchRecyclerItemArrayList.add(new SearchRecyclerItem(R.drawable.baseline_west_black_18dp, "Nine", "Line 2"));
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewAdapter(searchRecyclerItemArrayList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                UserData.getInstance().attraction = attractions.get(position);
                UserData.getInstance().attractionPhoto = attractionPictures.get(position);
                Intent attractionActivity = new Intent(ProfileActivity.this , AttractionActivity.class );
                startActivity(attractionActivity);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    public void setButtons(){
        buttonInsert = findViewById(R.id.button_insert);
        buttonRemove = findViewById(R.id.button_remove);
        editTextInsert = findViewById(R.id.edittext_insert);
        editTextRemove = findViewById(R.id.edittext_remove);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextInsert.getText().toString());
               // insertItem(position);
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position);
            }
        });
    }

    public void insertItem(int position, Bitmap picture , String name) {
        searchRecyclerItemArrayList.add(position, new SearchRecyclerItem(picture, name, ""));
        mAdapter.notifyItemInserted(position);
    }
    public void removeItem(int position) {
        searchRecyclerItemArrayList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void changeItem(int position, String text) {
        searchRecyclerItemArrayList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }

    private void filter(String text){
        ArrayList<SearchRecyclerItem> filteredList = new ArrayList<>();
        for (SearchRecyclerItem item : searchRecyclerItemArrayList) {
            if (item.getText1().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }

}