package rs.elfak.mosis.kristijan.heavenguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;

public class SettingsActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PROFILE = "tourist";
    public String profileP;

    private EditText settingsNewName;
    private Switch settingsGPS, settingsTheme;
    private Button settingsMakeChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        profileP = sharedPreferences.getString(PROFILE, "");

        settingsNewName = findViewById(R.id.settings_edit_name);
        settingsGPS = findViewById(R.id.settings_gps_sw);
        settingsTheme = findViewById(R.id.settings_theme_sw);
        settingsMakeChangesButton = findViewById(R.id.settings_change_button);

        if(profileP.equals("manager")){
            settingsNewName.setVisibility(View.GONE);
        }

        settingsMakeChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData.getInstance().name = settingsNewName.getText().toString();
                if(profileP.equals("tourist")){
                    DBService.getInstance().AddUser(UserData.getInstance().itsMeT);
                }
                if(profileP.equals("guide")){
                    DBService.getInstance().AddUser(UserData.getInstance().itsMeG);
                }
            }
        });
    }
}