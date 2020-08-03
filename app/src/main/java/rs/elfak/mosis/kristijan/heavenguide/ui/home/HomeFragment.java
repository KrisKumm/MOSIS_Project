package rs.elfak.mosis.kristijan.heavenguide.ui.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import rs.elfak.mosis.kristijan.heavenguide.R;
import rs.elfak.mosis.kristijan.heavenguide.data.UserData;
import rs.elfak.mosis.kristijan.heavenguide.service.FirebaseCallback;
import rs.elfak.mosis.kristijan.heavenguide.service.StorageService;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private ImageView avatar;
    private Bitmap picture;

    private TextView usernameLabel;
    private TextView roleLabel;

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

        StorageService.getInstance().downloadPhoto(UserData.getInstance().userType.toString(), UserData.getInstance().uId, UserData.getInstance().gmail, new FirebaseCallback() {
            @Override
            public void onCallback(Object object) {
                avatar.setImageBitmap(Bitmap.createScaledBitmap((Bitmap) object,  avatar.getWidth(), avatar.getHeight(),false));
            }
        });

        usernameLabel.setText(UserData.getInstance().name);
        roleLabel.setText(UserData.getInstance().userType.toString());

        return root;
    }
}