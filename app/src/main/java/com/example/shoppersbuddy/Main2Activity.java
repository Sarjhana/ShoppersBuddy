package com.example.shoppersbuddy;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.shoppersbuddy.ui.Stores;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Main2Activity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {

    String uuid,major,minor;
    public static int flag = 0;
    String id,store_name,store_offer;
    BeaconManager beaconManager;
    Region beaconRegion;
    private NotificationManager mNotificationManager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Stores stores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //requesting permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String []{Manifest.permission.ACCESS_COARSE_LOCATION},1234);
        }

        //initializing all the required classes
        beaconManager = BeaconManager.getInstanceForApplication(Main2Activity.this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

    }

    //Starts detecting the signal from nearby beacons
    public void startMonitoring(){

        try{
            beaconRegion = new Region("Mybeacons",null,null,null);
            beaconManager.startMonitoringBeaconsInRegion(beaconRegion);
            beaconManager.startRangingBeaconsInRegion(beaconRegion);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Boolean rangingMessageRaised = false;
    private Boolean entryMessageRaised = false;
    private Boolean exitMessageRaised = false;
    @Override
    public void onBeaconServiceConnect() {

        beaconManager.removeAllMonitorNotifiers();
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                //  showToast(region.getUniqueId());
                //                   text.setText(region.getUniqueId());
                entryMessageRaised = true;
            }

            @Override
            public void didExitRegion(Region region) {
                //                     text2.setText(region.getUniqueId());
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });

        //Identifies the identifiers of the detected beacon which is in the given range
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (!rangingMessageRaised && beacons != null && !beacons.isEmpty()) {
//                        showToast(region.getUniqueId());
                    // text2.setText(region.getUniqueId());
                }
                //showToast("entered");
                for (org.altbeacon.beacon.Beacon beacon : beacons) {

                    //UUID
                    uuid = String.valueOf(beacon.getId1());

                    //Major
                    major = String.valueOf(beacon.getId2());

                    //Minor
                    minor = String.valueOf(beacon.getId3());
                }
                //               text2.setText(uuid + major + minor);
                id = uuid + major + minor;


                rangingMessageRaised = true;

                try {
                    DatabaseReference myRef = database.getReference("store_info");
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                stores = ds.getValue(Stores.class);
                                if (ds.hasChild("uuid")) {
                                    if (id.equals(ds.child("uuid").getValue(String.class))) {
                                        store_name = stores.getBrand_name();
                                        store_offer = stores.getOffer();
                                        flag = 1;
                                        notification(flag);
                                        break;
                                    }
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


    }

    //scans whenever the app is resumed
    @Override
    protected void onResume() {
        super.onResume();
        startMonitoring();
    }

    public void notification(int flag)
    {

        if(flag == 1) {

          //  Toast.makeText(Main2Activity.this, store_name, Toast.LENGTH_LONG).show();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext(), "notify_001");
            Intent ii = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(store_offer);
            bigText.setBigContentTitle(store_name);
            bigText.setSummaryText("Text in detail");

            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            mBuilder.setContentTitle(store_name);
            mBuilder.setContentText(store_offer);
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);


            mNotificationManager =
                    (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mNotificationManager.createNotificationChannel(channel);
                }
                mBuilder.setChannelId(channelId);
            }

            mNotificationManager.notify(0, mBuilder.build());

            flag = 0;
        }
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

    }
}
