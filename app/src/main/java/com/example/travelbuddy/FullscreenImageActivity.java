package com.example.travelbuddy;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class FullscreenImageActivity extends AppCompatActivity {

    private ImageView fullscreenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);

        fullscreenImage = findViewById(R.id.fullscreenImage);

        String imageUriString = getIntent().getStringExtra("imageUri");

        if (imageUriString != null) {
            Glide.with(this)
                    .load(Uri.parse(imageUriString))
                    .into(fullscreenImage);
        }
    }
}
