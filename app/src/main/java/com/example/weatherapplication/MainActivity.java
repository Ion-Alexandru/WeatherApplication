package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    /*BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavgationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }*/

    private RecyclerView weekDayForecastRecyclerView;
    private WeekDayForecastAdapter weekDayForecastAdapter;
    private ArrayList<WeekDayForecast> weekDayForecastArrayList;

    private RecyclerView hourlyForecastRecyclerView;
    private HourlyForecastAdapter hourlyForecastAdapter;
    private ArrayList<HourlyForecast> hourlyForecastArrayList;

    private TextView cityNameWeather;
    private TextView temperatureTV;
    private TextView forecastTV;

    private ImageView forecastIV;
    private ImageView cityMenu;

    View weatherLayout;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SWITCH = "ok";

    String value;
    boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        cityNameWeather = findViewById(R.id.weather_city_name);
        temperatureTV = findViewById(R.id.temperature);
        forecastTV = findViewById(R.id.forecast);
        forecastIV = findViewById(R.id.weatherFragmentImage);
        weatherLayout = findViewById(R.id.weather_fragment_layout);
        cityMenu = findViewById(R.id.cityMenu);

        cityMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CityMenu.class);
                startActivity(i);
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            value = extras.getString("cityData");

            cityNameWeather.setText(value);
            getWeather(value);

            saveData();

        } else if(firstTime() == true){
            value = "Bucharest";

            cityNameWeather.setText(value);
            getWeather(value);
        }

        else {
            loadData();
            updateData();
        }

        final Handler handler = new Handler();
        final int delay = 300000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                getWeather(value);
                handler.postDelayed(this, delay);
            }
        }, delay);

        weekDayForecastRecyclerView = findViewById(R.id.weekdayForecast_recyclerview);
        weekDayForecastArrayList = new ArrayList<>();

        hourlyForecastRecyclerView = findViewById(R.id.hourlyForecast_recyclerview);
        hourlyForecastArrayList = new ArrayList<>();

        weekDayForecastAdapter = new WeekDayForecastAdapter(weekDayForecastArrayList, this);
        hourlyForecastAdapter = new HourlyForecastAdapter(hourlyForecastArrayList, this);

        LinearLayoutManager managerWeek = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        weekDayForecastRecyclerView.setHasFixedSize(true);
        weekDayForecastRecyclerView.setLayoutManager(managerWeek);
        weekDayForecastRecyclerView.setAdapter(weekDayForecastAdapter);

        LinearLayoutManager managerHour = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        hourlyForecastRecyclerView.setHasFixedSize(true);
        hourlyForecastRecyclerView.setLayoutManager(managerHour);
        hourlyForecastRecyclerView.setAdapter(hourlyForecastAdapter);
    }

    private void getWeather(String cityName){
        String url = "https://api.weatherapi.com/v1/forecast.json?key=16c28085aa0a4ecf9c3173621221004 &q=" + cityName + "&days=3&aqi=no&alerts=no";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                weekDayForecastArrayList.clear();
                hourlyForecastArrayList.clear();

                try{

                    Calendar calendar = Calendar.getInstance();
                    int calendarDay = calendar.get(Calendar.DAY_OF_WEEK);

                    double temperature = response.getJSONObject("current").getDouble("temp_c");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    temperatureTV.setText(temperature + "°C");

                    //int isDay = response.getJSONObject("current").getJSONObject("condition").getInt("is_day");

                    String forecast = response.getJSONObject("current").getJSONObject("condition").getString("text");

                    String currentIcon;
                    if(forecast.toLowerCase().contains("sun") && isDay == 1 && temperature >= 5.0) {
                        currentIcon = "sunny";
                    } else if(forecast.toLowerCase().contains("storm") || forecast.toLowerCase().contains("thunder") || forecast.toLowerCase().contains("thundery")){
                        currentIcon = "stormy";
                    } else if(forecast.toLowerCase().contains("rain") || forecast.toLowerCase().contains("pellets") || forecast.toLowerCase().contains("sleet") || forecast.toLowerCase().contains("drizzle")) {
                        currentIcon = "rainy";
                    } else if(forecast.toLowerCase() == "sunny" && isDay == 1 && temperature < 5.0) {
                        currentIcon = "cold_sunny";
                    } else if(forecast.toLowerCase().contains("clear")){
                        currentIcon = "moony";
                    } else if(forecast.toLowerCase().contains("partly") && isDay == 0) {
                        currentIcon = "cloudy_moony";
                    } else if(forecast.toLowerCase().contains("partly") && isDay == 1) {
                        currentIcon = "cloudy_sunny";
                    } else if (forecast.toLowerCase().contains("cloudy") || forecast.toLowerCase().contains("mist") || forecast.toLowerCase().contains("fog") || forecast.toLowerCase().contains("overcast")){
                        currentIcon = "cloudy";
                    } else if (forecast.toLowerCase().contains("snow") || forecast.toLowerCase().contains("blizzard")){
                        currentIcon = "snowy";
                    } else currentIcon = "ic_sun";

                    int iconID = getResources().getIdentifier(currentIcon , "drawable", getPackageName());

                    Picasso.get().load(iconID).into(forecastIV);
                    forecastTV.setText(forecast);

                    JSONObject forecastObj = response.getJSONObject("forecast");

                    for(int i = 0; i < 3 ; i++){
                        JSONObject forecastDay = forecastObj.getJSONArray("forecastday").getJSONObject(i);
                        JSONObject day = forecastDay.getJSONObject("day");
                        double averageTemp = day.getDouble("maxtemp_c");
                        String dayForecast = day.getJSONObject("condition").getString("text");
                        int dayOfTheWeek;
                        String displayDay = null;

                        if(i + calendarDay > 7){
                            dayOfTheWeek = i;
                        } else dayOfTheWeek = calendarDay + i;

                        if(dayOfTheWeek == 1){
                            displayDay = "Sunday";
                        } else if(dayOfTheWeek == 2){
                            displayDay = "Monday";
                        } else if(dayOfTheWeek == 3){
                            displayDay = "Tuesday";
                        } else if(dayOfTheWeek == 4){
                            displayDay = "Wednesday";
                        } else if(dayOfTheWeek == 5){
                            displayDay = "Thursday";
                        } else if(dayOfTheWeek == 6){
                            displayDay = "Friday";
                        } else if(dayOfTheWeek == 7){
                            displayDay = "Saturday";
                        }

                        String weekDayIcon;
                        if(dayForecast.toLowerCase().contains("sun") && averageTemp >= 5.0) {
                            weekDayIcon = "sunny";
                        } else if(dayForecast.toLowerCase().contains("storm") || dayForecast.toLowerCase().contains("thunder") || dayForecast.toLowerCase().contains("thundery")){
                            weekDayIcon = "stormy";
                        } else if(dayForecast.toLowerCase().contains("rain") || dayForecast.toLowerCase().contains("pellets") || dayForecast.toLowerCase().contains("sleet") || dayForecast.toLowerCase().contains("drizzle")) {
                            weekDayIcon = "rainy";
                        } else if(dayForecast.toLowerCase() == "sunny" && averageTemp < 5.0) {
                            weekDayIcon = "cold_sunny";
                        } else if(dayForecast.toLowerCase().contains("partly")) {
                            weekDayIcon = "cloudy_sunny";
                        } else if (dayForecast.toLowerCase().contains("cloudy") || dayForecast.toLowerCase().contains("mist") || dayForecast.toLowerCase().contains("fog") || dayForecast.toLowerCase().contains("overcast")){
                            weekDayIcon = "cloudy";
                        } else if (dayForecast.toLowerCase().contains("snow") || dayForecast.toLowerCase().contains("blizzard")){
                            weekDayIcon = "snowy";
                        } else weekDayIcon = "ic_sun";

                        weekDayForecastArrayList.add(new WeekDayForecast(averageTemp + "°C", displayDay, weekDayIcon, dayForecast));
                    }

                    JSONObject currentForecast = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = currentForecast.getJSONArray("hour");

                    for(int j = 0; j < hourArray.length(); j++){
                        JSONObject hourObj = hourArray.getJSONObject(j);
                        String hour = hourObj.getString("time");
                        int isDayHour = hourObj.getInt("is_day");;
                        int hourTemperature = hourObj.getInt("temp_c");
                        String hourForecast = hourObj.getJSONObject("condition").getString("text");

                        hour = hour.split(" ")[1];

                        String hourlyIcon;
                        if(hourForecast.toLowerCase().contains("sun") && hourTemperature >= 5.0) {
                            hourlyIcon = "sunny";
                        } else if(hourForecast.toLowerCase().contains("storm") || hourForecast.toLowerCase().contains("thunder") || hourForecast.toLowerCase().contains("thundery")){
                            hourlyIcon = "stormy";
                        } else if(hourForecast.toLowerCase().contains("rain") || hourForecast.toLowerCase().contains("pellets") || hourForecast.toLowerCase().contains("sleet") || hourForecast.toLowerCase().contains("drizzle")) {
                            hourlyIcon = "rainy";
                        } else if(hourForecast.toLowerCase() == "sunny" && hourTemperature < 5 && isDay == 1) {
                            hourlyIcon = "cold_sunny";
                        } else if(hourForecast.toLowerCase().contains("clear")){
                            hourlyIcon = "moony";
                        } else if(hourForecast.toLowerCase().contains("partly") && isDayHour == 0) {
                            hourlyIcon = "cloudy_moony";
                        } else if(hourForecast.toLowerCase().contains("partly") && isDayHour == 1) {
                            hourlyIcon = "cloudy_sunny";
                        } else if (hourForecast.toLowerCase().contains("cloudy") || hourForecast.toLowerCase().contains("mist") || hourForecast.toLowerCase().contains("fog") || hourForecast.toLowerCase().contains("overcast")){
                            hourlyIcon = "cloudy";
                        } else if (hourForecast.toLowerCase().contains("snow") || hourForecast.toLowerCase().contains("blizzard")){
                            hourlyIcon = "snowy";
                        } else hourlyIcon = "ic_sun";

                        hourlyForecastArrayList.add(new HourlyForecast(hourTemperature, hour, hourlyIcon, hourForecast));
                    }

                    if(isDay == 1){
                        //Picasso.get().load(R.drawable.day_background).into(backgroundIV);
                        weatherLayout.setBackground(getResources().getDrawable(R.drawable.day_background));
                    } else {
                        //Picasso.get().load(R.drawable.night_background).into(backgroundIV);
                        weatherLayout.setBackground(getResources().getDrawable(R.drawable.night_background));
                    }

                    weekDayForecastAdapter.notifyDataSetChanged();
                    hourlyForecastAdapter.notifyDataSetChanged();

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please choose the city again..", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, value);
        editor.putBoolean(SWITCH, ok);
        editor.apply();
    }

    public Boolean firstTime() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        ok = sharedPreferences.getBoolean(SWITCH, false);

        return ok;
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        value = sharedPreferences.getString(TEXT, "");
    }

    public void updateData() {
        cityNameWeather.setText(value);
        getWeather(value);
    }
}