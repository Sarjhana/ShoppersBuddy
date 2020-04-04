package com.example.shoppersbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppersbuddy.ui.Stores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconData;
import org.altbeacon.beacon.BeaconDataNotifier;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BleNotAvailableException;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer,RangeNotifier {
    String uuid,major,minor;
//    TextView text,text2;
//    Button start,stop,push;
    public static int flag = 0;
    String id,store_name,store_offer;
    BeaconManager beaconManager;
    Region beaconRegion;
    private NotificationManager mNotificationManager;

    public final String CHANNEL_ID="personal notif";
    public final int NOTIFICATION_ID = 001;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Stores stores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        start = findViewById(R.id.start);
//        stop = findViewById(R.id.stop);
//        push = findViewById(R.id.push);
//        text = findViewById(R.id.disp);
//        text2 = findViewById(R.id.text2);
//
//        push.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//
//
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    startMonitoring();
//            }
//        });
//
//        stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    stopMonitoring();
//            }
//        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String []{Manifest.permission.ACCESS_COARSE_LOCATION},1234);
        }

        beaconManager = BeaconManager.getInstanceForApplication(MainActivity.this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

    }

    public void startMonitoring(){

        try{
            beaconRegion = new Region("Mybeacons",null,null,null);
            beaconManager.startMonitoringBeaconsInRegion(beaconRegion);
            beaconManager.startRangingBeaconsInRegion(beaconRegion);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void  stopMonitoring(){

        try {

            beaconManager.stopMonitoringBeaconsInRegion(beaconRegion);
            beaconManager.stopRangingBeaconsInRegion(beaconRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Boolean entryMessageRaised = false;
    private Boolean exitMessageRaised = false;
    private Boolean rangingMessageRaised = false;

    public void showToast(String str){

        Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
        }




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

//    @Override
//    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
//
//
//        }

    @Override
    protected void onResume() {
        super.onResume();
        startMonitoring();

    }

    public void notification(int flag)
    {

        if(flag == 1) {

            Toast.makeText(MainActivity.this, store_name, Toast.LENGTH_LONG).show();
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

//        if (flag==0) {
//            notificationchecker();
//
//        }
//        else{
//            id = null;
//            flag = 0;
//        }
//




    public void notificationchecker()
    {


     //   flag =1;
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

    }
}


