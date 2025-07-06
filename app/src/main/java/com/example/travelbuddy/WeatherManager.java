package com.example.travelbuddy;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherManager {
    private static final String TAG = "WeatherManager";
    private static final String API_KEY = "64b90ba09da02d2145c843acdb3043a8";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    private OkHttpClient client;
    private Gson gson;

    public WeatherManager(Context context) {
        client = new OkHttpClient();
        gson = new Gson();
    }

    public interface WeatherCallback {
        void onSuccess(OpenWeatherResponse weatherData);
        void onError(String error);
    }

    public void getCurrentWeather(double latitude, double longitude, WeatherCallback callback) {
        String url = String.format("%s?lat=%f&lon=%f&appid=%s",
                BASE_URL, latitude, longitude, API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Weather API call failed", e);
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String errorMsg = "HTTP error: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Weather API response: " + responseBody);

                    OpenWeatherResponse weatherData = gson.fromJson(responseBody, OpenWeatherResponse.class);

                    if (weatherData != null && weatherData.getCod() == 200) {
                        callback.onSuccess(weatherData);
                    } else {
                        callback.onError("Invalid weather data received");
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Error parsing weather data", e);
                    callback.onError("Error parsing weather data: " + e.getMessage());
                }
            }
        });
    }

    public void getCurrentWeatherByCity(String cityName, WeatherCallback callback) {
        String url = String.format("%s?q=%s&appid=%s",
                BASE_URL, cityName, API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Weather API call failed", e);
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String errorMsg = "HTTP error: " + response.code();
                    Log.e(TAG, errorMsg);
                    callback.onError(errorMsg);
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Weather API response: " + responseBody);

                    OpenWeatherResponse weatherData = gson.fromJson(responseBody, OpenWeatherResponse.class);

                    if (weatherData != null && weatherData.getCod() == 200) {
                        callback.onSuccess(weatherData);
                    } else {
                        callback.onError("Invalid weather data received");
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Error parsing weather data", e);
                    callback.onError("Error parsing weather data: " + e.getMessage());
                }
            }
        });
    }
}