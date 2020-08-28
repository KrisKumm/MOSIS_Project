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
import rs.elfak.mosis.kristijan.heavenguide.data.model.Manager;
import rs.elfak.mosis.kristijan.heavenguide.data.model.SearchRecyclerItem;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;
import rs.elfak.mosis.kristijan.heavenguide.ui.login.LoginActivity;

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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
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
        searchRecyclerItemArrayList.add(new SearchRecyclerItem(R.drawable.baseline_double_arrow_black_18dp, "Line 1", "Line 2"));
        searchRecyclerItemArrayList.add(new SearchRecyclerItem(R.drawable.baseline_north_black_18dp, "Line 3", "Line 4"));
        searchRecyclerItemArrayList.add(new SearchRecyclerItem(R.drawable.baseline_south_black_18dp, "Line 5", "Line 6"));
        searchRecyclerItemArrayList.add(new SearchRecyclerItem(R.drawable.baseline_west_black_18dp, "Six", "Line 2"));
        searchRecyclerItemArrayList.add(new SearchRecyclerItem(R.drawable.baseline_west_black_18dp, "Seven", "Line 2"));
        searchRecyclerItemArrayList.add(new SearchRecyclerItem(R.drawable.baseline_west_black_18dp, "Eight", "Line 2"));
        searchRecyclerItemArrayList.add(new SearchRecyclerItem(R.drawable.baseline_west_black_18dp, "Nine", "Line 2"));
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
                changeItem(position, "Clicked");
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
                insertItem(position);
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

    public void insertItem(int position) {
        searchRecyclerItemArrayList.add(position, new SearchRecyclerItem(R.drawable.baseline_east_black_18dp, "New Item At Position" + position, "This is Line 2"));
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