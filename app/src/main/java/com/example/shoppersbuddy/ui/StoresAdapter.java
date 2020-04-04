package com.example.shoppersbuddy.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppersbuddy.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.StoresViewHolder> {
    private ArrayList<StoresItem>  StoresItemList;

    public static class StoresViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView name;
       // public TextView about;
        public TextView location;
      //  public TextView offers;

        public StoresViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
        //    about = view.findViewById(R.id.about);
            location = view.findViewById(R.id.location);
        }
    }

    public StoresAdapter(ArrayList<StoresItem> storesList){
        StoresItemList = storesList;
    }

    @NonNull
    @Override
    public StoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stores_item, parent, false);
        StoresViewHolder storesViewHolder = new StoresViewHolder(v);
        return storesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoresViewHolder holder, int position) {
        final StoresItem currentItem = StoresItemList.get(position);


     //   holder.imageView.setText(currentItem.getBrand_image());
        holder.name.setText(currentItem.getBrand_name());
      //  holder.about.setText(currentItem.getContent());
        holder.location.setText(currentItem.getLocation());
    //    holder.offers.setText(currentItem.getText2());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(view.getContext(),currentItem.getBrand_name(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(),StoreInfo.class);
                intent.putExtra("message",currentItem.getBrand_name());

                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return StoresItemList.size();
    }


}
