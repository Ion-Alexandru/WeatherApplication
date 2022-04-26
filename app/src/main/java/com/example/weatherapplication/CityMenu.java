package com.example.weatherapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CityMenu extends AppCompatActivity {

    private EditText searchCity;
    private ImageView addCity;
    private RecyclerView recyclerView;
    private CityAdapter cityAdapter;
    private ArrayList<City> cityArrayList;

    private boolean flag = false;

    private static final String LIST_KEY = "list_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_menu_layout);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recycle_view);

        cityArrayList = new ArrayList<>();
        cityArrayList = getListFromLocal(this);

        if(cityArrayList == null){
            cityArrayList = new ArrayList<>();
        }

        cityAdapter = new CityAdapter(this::onItemClick , cityArrayList, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        searchCity = findViewById(R.id.city_search);
        addCity = findViewById(R.id.add_searched_city);
        recyclerView = findViewById(R.id.recycle_view);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(cityAdapter);

        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = searchCity.getText().toString();

                if(cityName.isEmpty()){
                    Toast.makeText(CityMenu.this, "Please enter a city name first", Toast.LENGTH_SHORT).show();
                } else {
                    getWeatherInfo(cityName);
                }
            }
        });
    }

    public void onItemClick(int position) {
        Toast.makeText(CityMenu.this, cityArrayList.get(position).getCityName() + ", " + cityArrayList.get(position).getCityCountry() + " selected!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(CityMenu.this, MainActivity.class);
        intent.putExtra("cityData", cityArrayList.get(position).getCityName());
        startActivity(intent);
        finish();
    }

    public void saveListInLocal(Context context, ArrayList<City> list) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(LIST_KEY, json);
        editor.apply();
    }

    public ArrayList<City> getListFromLocal(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(LIST_KEY, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<City>>() {}.getType();
        ArrayList<City> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    public void getWeatherInfo(String cityName){

        String url = "https://api.weatherapi.com/v1/forecast.json?key=16c28085aa0a4ecf9c3173621221004 &q=" + cityName + "&days=3&aqi=no&alerts=no";

        RequestQueue requestQueue = Volley.newRequestQueue(CityMenu.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    String countryName = response.getJSONObject("location").getString("country");
                    String forecast = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    double temperature = response.getJSONObject("current").getDouble("temp_c");

                    String currentIcon;
                    if(forecast.toLowerCase().contains("sun") && isDay == 1 && temperature >= 5.0) {
                        currentIcon = "sunny";
                    } else if(forecast.toLowerCase().contains("rain") || forecast.toLowerCase().contains("pellets") || forecast.toLowerCase().contains("sleet") || forecast.toLowerCase().contains("drizzle")) {
                        currentIcon = "rainy";
                    } else if(forecast.toLowerCase().contains("storm") || forecast.toLowerCase().contains("thunder") || forecast.toLowerCase().contains("thundery")){
                        currentIcon = "stormy";
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

                    for(City city : cityArrayList){
                        if(city.getCityName() == cityName){
                            flag = true;
                        }
                    }

                    if(flag == false){
                        cityArrayList.add(new City(cityName, countryName, currentIcon, isDay));
                        saveListInLocal(getApplicationContext(), cityArrayList);
                        cityAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(CityMenu.this, "You already have that city in your list", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CityMenu.this, "Please choose the city again..", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            cityArrayList.remove(viewHolder.getAdapterPosition());
            saveListInLocal(getApplicationContext(), cityArrayList);
            cityAdapter.notifyDataSetChanged();
        }
    };
}
