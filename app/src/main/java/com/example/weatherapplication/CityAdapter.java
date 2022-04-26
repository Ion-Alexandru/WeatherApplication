package com.example.weatherapplication;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    // creating a variable for array list and context.
    private ArrayList<City> CityArrayList;
    private Context context;

    // creating a constructor for our variables.
    public CityAdapter(RecyclerViewInterface recyclerViewInterface, ArrayList<City> CityArrayList, Context context) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.CityArrayList = CityArrayList;
        this.context = context;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<City> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        CityArrayList = filterllist;
        // below line is to notify our adapter 
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        City modal = CityArrayList.get(position);
        holder.cityNameView.setText(modal.getCityName());
        holder.cityCountryView.setText(modal.getCityCountry());
        String iconValue = modal.getCityForecast();

        int resID = context.getResources().getIdentifier(iconValue , "drawable", context.getPackageName());

        Picasso.get().load(resID).into(holder.cityForecast);

        if(modal.getIsDay() == 1){
            //Picasso.get().load(R.drawable.day_background).into(backgroundIV);
            holder.cityCard.setBackground(context.getResources().getDrawable(R.drawable.day_background));
        } else {
            //Picasso.get().load(R.drawable.night_background).into(backgroundIV);
            holder.cityCard.setBackground(context.getResources().getDrawable(R.drawable.night_background));
        }
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return CityArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our views.
        private TextView cityNameView, cityCountryView;
        private ImageView cityForecast;
        private View cityCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            cityNameView = itemView.findViewById(R.id.city_name);
            cityCountryView = itemView.findViewById(R.id.city_country);
            cityForecast = itemView.findViewById(R.id.city_card_icon);
            cityCard = itemView.findViewById(R.id.city_card_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int position =  getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}