package com.example.weatherapplication;

public class City {

    private String cityName;
    private String cityCountry;
    private String cityForecast;
    private int isDay;

    public City(String cityName, String cityCountry, String cityForecast, int isDay){
        this.cityCountry = cityCountry;
        this.cityName = cityName;
        this.cityForecast = cityForecast;
        this.isDay = isDay;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public String getCityForecast() {
        return cityForecast;
    }

    public int getIsDay() {
        return isDay;
    }
}
