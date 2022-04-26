package com.example.weatherapplication;

import android.graphics.drawable.Icon;
import android.media.Image;

public class HourlyForecast {

    private int temperature;
    private String hour;
    private String weatherIcon;
    private String forecast;

    public HourlyForecast(int temperature, String hour, String weatherIcon, String forecast) {
        this.temperature = temperature;
        this.hour = hour;
        this.weatherIcon = weatherIcon;
        this.forecast = forecast;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getHour() {
        return hour;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public String getForecast() {
        return forecast;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
