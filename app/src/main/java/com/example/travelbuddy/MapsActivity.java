package com.example.travelbuddy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private EditText searchLocation;
    private Button searchButton;
    private LatLng currentLocation;

    private TextView locationName, locationDistance, locationAddress;
    private CardView locationInfoCard;
    private FloatingActionButton btnCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        searchLocation = findViewById(R.id.search_location);
        searchButton = findViewById(R.id.search_button);

        locationName = findViewById(R.id.locationName);
        locationDistance = findViewById(R.id.locationDistance);
        locationAddress = findViewById(R.id.locationAddress);
        locationInfoCard = findViewById(R.id.locationInfoCard);

        btnCurrentLocation = findViewById(R.id.btnCurrentLocation);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchButton.setOnClickListener(v -> {
            String locationName = searchLocation.getText().toString().trim();
            if (!locationName.isEmpty()) {
                searchForLocation(locationName);
            } else {
                Toast.makeText(this, "Please enter your location.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCurrentLocation.setOnClickListener(v -> {
            if (currentLocation != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            } else {
                Toast.makeText(this, "Current location not available.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchForLocation(String locationName) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                Toast.makeText(this, "Location not found.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Location search error.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("You Are Here"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
                    } else {
                        LatLng defaultLatLng = new LatLng(3.0738, 101.5153); // Shah Alam center
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 13));
                    }
                });

        addFixedLocations();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(3.0738, 101.5153), 13));

        mMap.setOnMarkerClickListener(marker -> {
            LatLng markerLocation = marker.getPosition();

            if (currentLocation != null) {
                float[] results = new float[1];
                Location.distanceBetween(
                        currentLocation.latitude, currentLocation.longitude,
                        markerLocation.latitude, markerLocation.longitude,
                        results);
                float distanceInMeters = results[0];

                Geocoder geocoder = new Geocoder(this);
                String addressText = "Address not found";
                try {
                    List<Address> addresses = geocoder.getFromLocation(
                            markerLocation.latitude, markerLocation.longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        addressText = addresses.get(0).getAddressLine(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                locationName.setText(marker.getTitle());
                locationDistance.setText("Distance: " + String.format("%.2f", distanceInMeters) + " meter");
                locationAddress.setText("Address: " + addressText);
                locationInfoCard.setVisibility(View.VISIBLE);
            }
            return false;
        });
    }

    private void addFixedLocations() {
        addMarker(new LatLng(3.0729, 101.4992), "Tasik Seksyen 7", BitmapDescriptorFactory.HUE_AZURE);
        addMarker(new LatLng(3.0735, 101.5158), "Tasik Seksyen 14", BitmapDescriptorFactory.HUE_GREEN);
        addMarker(new LatLng(3.0855, 101.5223), "Taman Botani Negara", BitmapDescriptorFactory.HUE_ORANGE);
        addMarker(new LatLng(3.0738, 101.5153), "Masjid Sultan Salahuddin Abdul Aziz Shah", BitmapDescriptorFactory.HUE_VIOLET);
        addMarker(new LatLng(3.0765, 101.5156), "Muzium Sultan Alam Shah", BitmapDescriptorFactory.HUE_YELLOW);
        addMarker(new LatLng(3.0707, 101.5246), "Perpustakaan Raja Tun Uda", BitmapDescriptorFactory.HUE_BLUE);
    }

    private void addMarker(LatLng latLng, String title, float color) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(color)));
    }
}
