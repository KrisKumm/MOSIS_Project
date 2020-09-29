package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PROFILE = "tourist";
    public String profileP;

    private EditText settingsNewName;
    private Switch settingsTheme;
    private Button settingsMakeChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        profileP = sharedPreferences.getString(PROFILE, "");

        settingsNewName = findViewById(R.id.settings_edit_name);
        settingsTheme = findViewById(R.id.settings_theme_sw);
        settingsMakeChangesButton = findViewById(R.id.settings_change_button);

        if(UserData.getInstance().userType == userType.manager){
            settingsNewName.setVisibility(View.GONE);
        }

        settingsMakeChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData.getInstance().name = settingsNewName.getText().toString();
                if(UserData.getInstance().userType == userType.tourist){
                    DBService.getInstance().AddUser(UserData.getInstance().itsMeT);
                }
                if(UserData.getInstance().userType == userType.guide){
                    DBService.getInstance().AddUser(UserData.getInstance().itsMeG);
                }
                Toast.makeText(SettingsActivity.this, "You have changed your name!", Toast.LENGTH_LONG).show();
            }
        });

        settingsTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nightModeFlags =  SettingsActivity.this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                    default:
                        break;
                }

            }
        });

    }

}