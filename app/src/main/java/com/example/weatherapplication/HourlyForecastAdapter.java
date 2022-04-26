package com.example.weatherapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {

    private ArrayList<HourlyForecast> hourlyForecastArrayList;
    private Context context;

    public HourlyForecastAdapter(ArrayList<HourlyForecast> hourlyForecastArrayList, Context context) {
        this.hourlyForecastArrayList = hourlyForecastArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyForecast modal = hourlyForecastArrayList.get(position);
        holder.hourlyForecastTemperatureView.setText(modal.getTemperature() + "°C");
        holder.hourlyForecastHourView.setText(modal.getHour());
        holder.hourlyForecastForecast.setText(modal.getForecast());

        String iconValue = modal.getWeatherIcon();

        int resID = context.getResources().getIdentifier(iconValue , "drawable", context.getPackageName());

        Picasso.get().load(resID).into(holder.hourlyForecastImageView);
    }

    @Override
    public int getItemCount() {
        return hourlyForecastArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView hourlyForecastTemperatureView;
        TextView hourlyForecastHourView;
        TextView hourlyForecastForecast;
        ImageView hourlyForecastImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hourlyForecastTemperatureView = itemView.findViewById(R.id.hourlyForecast_temperature);
            hourlyForecastHourView = itemView.findViewById(R.id.hourlyForecast_hour);
            hourlyForecastImageView = itemView.findViewById(R.id.hourlyForecast_icon);
            hourlyForecastForecast = itemView.findViewById(R.id.hourlyForecast);
        }
    }
}
