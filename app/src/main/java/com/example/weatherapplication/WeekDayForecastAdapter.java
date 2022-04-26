package com.example.weatherapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeekDayForecastAdapter extends RecyclerView.Adapter<WeekDayForecastAdapter.ViewHolder> {

    // creating a variable for array list and context.
    private ArrayList<WeekDayForecast> weekDayForecastArrayList;
    private Context context;

    // creating a constructor for our variables.
    public WeekDayForecastAdapter(ArrayList<WeekDayForecast> weekDayForecastArrayList, Context context) {
        this.weekDayForecastArrayList = weekDayForecastArrayList;
        this.context = context;
    }

    // method for filtering our recyclerview items.
    public void filterList(ArrayList<WeekDayForecast> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        weekDayForecastArrayList = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeekDayForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekday_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekDayForecastAdapter.ViewHolder holder, int position) {
        // setting data to our views of recycler view.
        WeekDayForecast modal = weekDayForecastArrayList.get(position);
        holder.weekDayForecastDegreeView.setText(modal.getDegree());
        holder.weekDayForecastDayView.setText(modal.getDay());
        holder.weekDayForecastForecast.setText(modal.getForecast());

        String iconValue = modal.getIcon();

        int resID = context.getResources().getIdentifier(iconValue , "drawable", context.getPackageName());

        //Log.d("ID", "onBindViewHolder: " + resID);

        Picasso.get().load(resID).into(holder.weekDayForecastIconView);
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return weekDayForecastArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our views.
        private TextView weekDayForecastDegreeView, weekDayForecastDayView, weekDayForecastForecast;
        private ImageView weekDayForecastIconView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            weekDayForecastDegreeView = itemView.findViewById(R.id.weekdayDegree);
            weekDayForecastDayView = itemView.findViewById(R.id.weekdayDay);
            weekDayForecastIconView = itemView.findViewById(R.id.weekdayIcon);
            weekDayForecastForecast = itemView.findViewById(R.id.weekdayForecast);
        }
    }
}