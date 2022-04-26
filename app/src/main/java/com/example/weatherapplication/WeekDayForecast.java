package com.example.weatherapplication;

import android.graphics.drawable.Icon;
import android.media.Image;
import android.widget.ImageView;

public class WeekDayForecast {

    private String forecast;
    private String day;
    private String icon;
    private String forecastText;

    public WeekDayForecast(String forecast, String day, String icon, String forecastText) {
        this.forecast = forecast;
        this.day = day;
        this.icon = icon;
        this.forecastText = forecastText;
    }

    public String getDegree() {
        return forecast;
    }

    public String getDay() {
        return day;
    }

    public void setDegree(String degree) {
        this.forecast = degree;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getForecast() {
        return forecastText;
    }
}
