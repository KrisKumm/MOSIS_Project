package rs.elfak.mosis.kristijan.heavenguide.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rs.elfak.mosis.kristijan.heavenguide.ProfileActivity;
import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.RegisterActivity;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Manager;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;
import rs.elfak.mosis.kristijan.heavenguide.data.model.userType;
import rs.elfak.mosis.kristijan.heavenguide.service.DBService;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PROFILE = "profile";
    public String profileP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        mAuth = FirebaseAuth.getInstance();

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        usernameEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                //loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser curUser = mAuth.getCurrentUser();
                            getLoggedInUserData(curUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

            }
        });

        setRegisterBtnHandler(registerButton, loadingProgressBar);

       FirebaseUser curUser = mAuth.getCurrentUser();
       if(curUser != null)
            getLoggedInUserData(curUser);

    }
    private void getLoggedInUserData(FirebaseUser curUser){

        UserData.getInstance().uId = curUser.getUid();
        UserData.getInstance().gmail = curUser.getEmail();

        final Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
            DBService.getInstance().GetGuide(UserData.getInstance().uId, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    TourGuide usr = (TourGuide) object;
                    if(usr != null) {
                        UserData.getInstance().name = usr.getName();
                        UserData.getInstance().userType = userType.guide;
                        UserData.getInstance().friends = usr.getFriends();
                        UserData.getInstance().notifications = usr.notifications;
                        UserData.getInstance().tourGroupId = usr.getMyTourGroup();
                        UserData.getInstance().myTours = usr.getMyTours();
                        UserData.getInstance().itsMeG = usr;
                        setSharedPreference("guide");
                        startActivity(i);
                        LoginActivity.this.finish();
                    }
                }
            });
            DBService.getInstance().GetUser(UserData.getInstance().uId, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    User usr = (User) object;
                    if(usr != null) {
                        UserData.getInstance().name = usr.getName();
                        UserData.getInstance().userType = userType.tourist;
                        UserData.getInstance().friends = usr.getFriends();
                        UserData.getInstance().notifications = usr.notifications;
                        UserData.getInstance().tourGroupId = usr.getMyTourGroup();
                        UserData.getInstance().myTours = usr.getMyTours();
                        UserData.getInstance().itsMeT = usr;
                        setSharedPreference("tourist");
                        startActivity(i);
                        LoginActivity.this.finish();
                    }
                }
            });
            DBService.getInstance().GetManager(UserData.getInstance().uId, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    Manager usr = (Manager) object;
                    if(usr != null){
                        UserData.getInstance().name = usr.getName();
                        UserData.getInstance().userType = userType.manager;
                        UserData.getInstance().notifications = usr.notifications;
                        UserData.getInstance().itsMeM = usr;
                        setSharedPreference("manager");
                        startActivity(i);
                        LoginActivity.this.finish();
                    }
                }
            });
    }

    private void setSharedPreference(String userType) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROFILE, userType);
        editor.apply();
        Toast.makeText(LoginActivity.this, userType+ " has logged in", Toast.LENGTH_SHORT).show();
    }

    private void setRegisterBtnHandler(Button registerButton, final ProgressBar loadingProgressBar ){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}