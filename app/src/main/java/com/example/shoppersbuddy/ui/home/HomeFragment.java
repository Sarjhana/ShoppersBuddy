package com.example.shoppersbuddy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppersbuddy.R;
import com.example.shoppersbuddy.ui.Stores;
import com.example.shoppersbuddy.ui.StoresAdapter;
import com.example.shoppersbuddy.ui.StoresItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private HomeViewModel homeViewModel;

    Spinner spinner;
    Stores stores;
    StoresItem storesItem;
    ArrayList<StoresItem> list = new ArrayList<>();
    ArrayList<Stores> totalList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("store_info");
  //  TextView t1,t2,t3,t4;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

   // ArrayList<Stores> total_list = new ArrayList<>();

    private String image1,name1,location1;
    // View v;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        spinner= root.findViewById(R.id.spinner);
        spinner.setPrompt("Category");
        // Spinner Drop down elements
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Category");
        categories.add("Food");
        categories.add("Clothing");
        categories.add("Entertainment");
        categories.add("Electronics");
        categories.add("Cosmetics");
        storesItem = new StoresItem();

//        t1 = root.findViewById(R.id.text1);
//        t2 = root.findViewById(R.id.text2);
//        t3 = root.findViewById(R.id.text3);
//        t4 = root.findViewById(R.id.text4);
//        stores = new Stores();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    stores = ds.getValue(Stores.class);

                    image1 = stores.getBrand_image();
                    name1 = stores.getBrand_name();
                    //about1 = stores.getContent();
                    location1 = stores.getLocation();

                    list.add(new StoresItem(image1,name1,location1));
                    totalList.add(stores);
//                    list.add(stores.getBrand_name());
                }


              //  Toast.makeText(getActivity(),stores.getOffer(),Toast.LENGTH_LONG).show();
//                t1.setText(list.get(0));
//                t2.setText(list.get(1));
//                t3.setText(list.get(2));
//                t4.setText(list.get(3));
//

                recyclerView = root.findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getActivity());
                adapter = new StoresAdapter(list);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        list.add(new StoresItem("image","name","about","location"));
//
//        list.add(new StoresItem("image2","name2","about2","location2"));
//
//        list.add(new StoresItem("image3","name3","about3","location3"));
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        spinner.setOnItemSelectedListener(this);
        return root;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
      //  Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}