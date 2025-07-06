package com.example.travelbuddy;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("main")
    public Main main;

    @SerializedName("wind")
    public Wind wind;

    @SerializedName("name")
    public String name;

    public static class Weather {
        @SerializedName("main")
        public String main;

        @SerializedName("description")
        public String description;

        @SerializedName("icon")
        public String icon;
    }

    public static class Main {
        @SerializedName("temp")
        public double temp;

        @SerializedName("humidity")
        public int humidity;
    }

    public static class Wind {
        @SerializedName("speed")
        public double speed;
    }
}
