package rs.elfak.mosis.kristijan.heavenguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Manager;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;
import rs.elfak.mosis.kristijan.heavenguide.ui.login.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ImageView avatar;
    private Bitmap picture;

    private TextView usernameLabel;
    private TextView roleLabel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        avatar = findViewById(R.id.imageViewAvatar);
        usernameLabel = findViewById(R.id.Username_label);
        roleLabel = findViewById(R.id.Role_label);

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

//        getUserData(new FirebaseCallback() {
//            @Override
//            public void onCallback(Object object) {
//                usernameLabel.setText(((UserData) object).name);
//                roleLabel.setText(((UserData) object).userType.toString());
//            }
//        });

//        StorageService.getInstance().downloadPhoto("users", UserData.getInstance().uId, UserData.getInstance().gmail, new FirebaseCallback() {
//            @Override
//            public void onCallback(Object object) {
//                avatar.setImageBitmap(Bitmap.createScaledBitmap((Bitmap) object,  avatar.getWidth(), avatar.getHeight(),false));
//            }
//        });
//
//        usernameLabel.setText(UserData.getInstance().name);
//        roleLabel.setText(UserData.getInstance().userType.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
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
                //Open search!
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

//    public void getUserData(final FirebaseCallback firebaseCallback){
//
//        if(UserData.getInstance().userType == userType.guide){
//            DBService.getInstance().GetGuide(UserData.getInstance().uId, new FirebaseCallback() {
//                @Override
//                public void onCallback(Object object) {
//                    TourGuide usr = (TourGuide) object;
//                    UserData.getInstance().name = usr.getName();
//                    firebaseCallback.onCallback(UserData.getInstance());
//                }
//            });
//        }
//        else if(UserData.getInstance().userType == userType.tourist){
//            DBService.getInstance().GetUser(UserData.getInstance().uId, new FirebaseCallback() {
//                @Override
//                public void onCallback(Object object) {
//                    User usr = (User) object;
//                    UserData.getInstance().name = usr.getName();
//                    firebaseCallback.onCallback(UserData.getInstance());
//                }
//            });
//        }
//        else{
//            DBService.getInstance().GetManager(UserData.getInstance().uId, new FirebaseCallback() {
//                @Override
//                public void onCallback(Object object) {
//                    Manager usr = (Manager) object;
//                    UserData.getInstance().name = usr.getName();
//                    UserData.getInstance().userType = userType.manager;
//                    firebaseCallback.onCallback(UserData.getInstance());
//                }
//            });
//        }
//    }
}

