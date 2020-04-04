package com.example.shoppersbuddy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppersbuddy.MainActivity;
import com.example.shoppersbuddy.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StoreInfo extends AppCompatActivity {

    Stores stores;
    String dummy;
    TextView name,about,offers,location;
    String brand_name;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("store_info");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        name = findViewById(R.id.name);
        about = findViewById(R.id.about);
        offers = findViewById(R.id.offers);
        location = findViewById(R.id.location);

        Intent intent = getIntent();
       brand_name = intent.getStringExtra("message");
        Toast.makeText(this,brand_name,Toast.LENGTH_LONG).show();
//        name.setText(brand_name);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot getdata : dataSnapshot.getChildren()){

                    stores = getdata.getValue(Stores.class);

                        if(brand_name.equals(stores.getBrand_name())) {
                            name.setText(stores.getBrand_name());
                            about.setText(stores.getContent());
                            offers.setText(stores.getOffer());
                            location.setText(stores.getLocation());
                            break;
                        }

                }
                Toast.makeText(StoreInfo.this,stores.getOffer(),Toast.LENGTH_LONG).show();

                // name.setText(stores.getBrand_name());
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
