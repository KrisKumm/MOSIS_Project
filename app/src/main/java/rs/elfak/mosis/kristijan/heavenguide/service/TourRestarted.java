package rs.elfak.mosis.kristijan.heavenguide.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import rs.elfak.mosis.kristijan.heavenguide.data.UserData;

public class TourRestarted extends BroadcastReceiver {
    private String groupId;
    private String myuid;
    private String userType;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
        groupId = intent.getExtras().getString("TOUR_GROUP");
        myuid = intent.getExtras().getString("MY_UID");
        userType = intent.getExtras().getString("USER_TYPE");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            Intent service = new Intent(context, TourService.class);
            service.putExtra("TOUR_GROUP", UserData.getInstance().tourGroupId);
            service.putExtra("MY_UID", UserData.getInstance().uId);
            service.putExtra("USER_TYPE", UserData.getInstance().userType.toString());
            context.startService(service);
        }
    }
}
