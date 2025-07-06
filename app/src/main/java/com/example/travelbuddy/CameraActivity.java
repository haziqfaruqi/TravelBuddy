package com.example.travelbuddy;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private ImageView imageView;
    private EditText descriptionEditText;
    private Button captureBtn, saveBtn;
    private Uri imageUri;

    private CardView weatherCard;
    private ProgressBar weatherProgressBar;
    private ImageView weatherIcon;
    private TextView temperatureText, weatherDescription, locationText;
    private TextView humidityText, windText;

    private WeatherManager weatherManager;
    private FusedLocationProviderClient fusedLocationClient;
    private SQLiteHelper databaseHelper;

    private OpenWeatherResponse currentWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initializeViews();
        initializeServices();
        setupClickListeners();
        requestLocationAndWeather();
    }

    private void initializeViews() {
        imageView = findViewById(R.id.imageView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        captureBtn = findViewById(R.id.captureBtn);
        saveBtn = findViewById(R.id.saveBtn);

        weatherCard = findViewById(R.id.weatherCard);
        weatherProgressBar = findViewById(R.id.weatherProgressBar);
        weatherIcon = findViewById(R.id.weatherIcon);
        temperatureText = findViewById(R.id.temperatureText);
        weatherDescription = findViewById(R.id.weatherDescription);
        locationText = findViewById(R.id.locationText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);
    }

    private void initializeServices() {
        weatherManager = new WeatherManager(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseHelper = new SQLiteHelper(this);
    }

    private void setupClickListeners() {
        captureBtn.setOnClickListener(v -> openCamera());
        saveBtn.setOnClickListener(v -> {
            if (imageUri != null) {
                getLocationAndSave();
            } else {
                Toast.makeText(this, "Capture a photo first!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestLocationAndWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            getCurrentLocationAndWeather();
        }
    }

    private void getCurrentLocationAndWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            fetchWeatherData(location.getLatitude(), location.getLongitude());
                        } else {
                            weatherProgressBar.setVisibility(View.GONE);
                            weatherDescription.setText("Location unavailable");
                        }
                    });
        }
    }

    private void fetchWeatherData(double latitude, double longitude) {
        weatherManager.getCurrentWeather(latitude, longitude, new WeatherManager.WeatherCallback() {
            @Override
            public void onSuccess(OpenWeatherResponse weather) {
                currentWeatherData = weather;
                runOnUiThread(() -> updateWeatherUI(weather));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    weatherProgressBar.setVisibility(View.GONE);
                    weatherDescription.setText("Weather unavailable");
                    Toast.makeText(CameraActivity.this, "Weather error: " + error,
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void updateWeatherUI(OpenWeatherResponse weather) {
        weatherProgressBar.setVisibility(View.GONE);
        weatherCard.setVisibility(View.VISIBLE);

        int temp = (int) Math.round(weather.getMain().getTemp() - 273.15);
        temperatureText.setText(temp + "°C");

        if (!weather.getWeather().isEmpty()) {
            String description = weather.getWeather().get(0).getDescription();
            weatherDescription.setText(capitalizeFirst(description));

            String mainWeather = weather.getWeather().get(0).getMain().toLowerCase();
            setWeatherIcon(mainWeather);
        }

        locationText.setText(weather.getName());
        humidityText.setText("Humidity: " + weather.getMain().getHumidity() + "%");
        windText.setText("Wind: " + String.format("%.1f", weather.getWind().getSpeed()) + " m/s");
    }

    private void setWeatherIcon(String weatherCondition) {
        int iconResource;
        switch (weatherCondition) {
            case "clear":
                iconResource = R.drawable.ic_weather_sunny;
                break;
            case "clouds":
                iconResource = R.drawable.ic_weather_cloudy;
                break;
            case "rain":
                iconResource = R.drawable.ic_weather_rainy;
                break;
            case "snow":
                iconResource = R.drawable.ic_weather_snowy;
                break;
            case "thunderstorm":
                iconResource = R.drawable.ic_weather_stormy;
                break;
            default:
                iconResource = R.drawable.ic_weather_sunny;
                break;
        }
        weatherIcon.setImageResource(iconResource);
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "TravelBuddyPic_" + System.currentTimeMillis());
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            imageView.setImageURI(imageUri);
        }
    }

    private void getLocationAndSave() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String path = imageUri.toString();
                        String description = descriptionEditText.getText().toString().trim();

                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        String locationName = "Unknown Location";
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if (!addresses.isEmpty() && addresses.get(0).getLocality() != null) {
                                locationName = addresses.get(0).getLocality();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        int photoNumber = databaseHelper.getRecordCount() + 1;
                        String photoLabel = "Photo " + photoNumber;

                        String fullDescription = description;
                        if (currentWeatherData != null) {
                            String weatherInfo = getWeatherSummary();
                            fullDescription = description + "\n" + weatherInfo;
                        }

                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        databaseHelper.addNewRecord(userId, path, locationName, fullDescription, photoLabel);

                        Toast.makeText(CameraActivity.this, "Photo saved with weather data!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to get location.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getWeatherSummary() {
        if (currentWeatherData == null) return "";

        int temp = (int) Math.round(currentWeatherData.getMain().getTemp() - 273.15);
        String description = !currentWeatherData.getWeather().isEmpty() ?
                currentWeatherData.getWeather().get(0).getDescription() : "N/A";
        int humidity = currentWeatherData.getMain().getHumidity();
        double windSpeed = currentWeatherData.getWind().getSpeed();

        return String.format("Weather: %d°C, %s (Humidity: %d%%, Wind: %.1f m/s)",
                temp, capitalizeFirst(description), humidity, windSpeed);
    }
}
