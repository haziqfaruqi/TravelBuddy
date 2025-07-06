package com.example.travelbuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchWeatherActivity extends AppCompatActivity {

    private EditText etCityName;
    private Button btnSearchCity;
    private LinearLayout weatherCard;
    private TextView tvWeatherLocation, tvWeatherDetails;

    private WeatherManager weatherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_weather);

        etCityName = findViewById(R.id.etCityName);
        btnSearchCity = findViewById(R.id.btnSearchCity);
        weatherCard = findViewById(R.id.weatherCard);
        tvWeatherLocation = findViewById(R.id.tvWeatherLocation);
        tvWeatherDetails = findViewById(R.id.tvWeatherDetails);

        weatherManager = new WeatherManager(this);

        btnSearchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = etCityName.getText().toString().trim();
                if (!city.isEmpty()) {
                    fetchWeatherByCity(city);
                } else {
                    Toast.makeText(SearchWeatherActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchWeatherByCity(String cityName) {
        weatherManager.getCurrentWeatherByCity(cityName, new WeatherManager.WeatherCallback() {
            @Override
            public void onSuccess(OpenWeatherResponse weatherData) {
                runOnUiThread(() -> {
                    weatherCard.setVisibility(View.VISIBLE);
                    tvWeatherLocation.setText("Weather in " + weatherData.getName());
                    String result = "Temperature: " + Math.round(weatherData.getMain().getTemp() - 273.15) + "°C"
                            + "\nHumidity: " + weatherData.getMain().getHumidity() + "%"
                            + "\nWind: " + weatherData.getWind().getSpeed() + " m/s"
                            + "\nCondition: " + weatherData.getWeather().get(0).getDescription();
                    tvWeatherDetails.setText(result);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(SearchWeatherActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    weatherCard.setVisibility(View.GONE);
                });
            }
        });
    }
}
