package com.example.travelbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView openMapBtn, logoutBtn, cameraBtn, galleryBtn, searchWeatherBtn, profileBtn;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // Initialize all ImageViews and TextView
        openMapBtn = findViewById(R.id.openMapBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        welcomeText = findViewById(R.id.welcomeText);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        searchWeatherBtn = findViewById(R.id.searchWeatherBtn);
        profileBtn = findViewById(R.id.profileBtn);

        // Open MapsActivity
        openMapBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
        });

        // Open CameraActivity
        cameraBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CameraActivity.class));
        });

        // Open GalleryActivity
        galleryBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GalleryActivity.class));
        });

        // Open SearchWeatherActivity
        searchWeatherBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SearchWeatherActivity.class));
        });

        // Open ProfileActivity
        profileBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });

        // Log out
        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}
