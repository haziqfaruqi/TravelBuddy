package com.example.travelbuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserEmail, tvUserName;
    private ImageView ivProfileImage;
    private Button btnChangePicture, btnEditName;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Init view refs
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserName = findViewById(R.id.tvUserName);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        btnChangePicture = findViewById(R.id.btnChangePicture);
        btnEditName = findViewById(R.id.btnEditName);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            tvUserEmail.setText(currentUser.getEmail());
            String displayName = currentUser.getDisplayName();
            tvUserName.setText((displayName != null && !displayName.isEmpty()) ? displayName : "No name set");

            // Load profile image if exists
            Uri photoUri = currentUser.getPhotoUrl();
            if (photoUri != null) {
                Glide.with(this).load(photoUri).into(ivProfileImage);
            }
        }

        // Handle name edit
        btnEditName.setOnClickListener(v -> showEditNameDialog());

        // Handle profile picture change
        btnChangePicture.setOnClickListener(v -> openImagePicker());

        // Setup image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            updateProfilePhoto(selectedImageUri);
                        }
                    }
                });
    }

    private void showEditNameDialog() {
        final EditText input = new EditText(this);
        input.setHint("Enter new name");

        new AlertDialog.Builder(this)
                .setTitle("Edit Name")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        updateDisplayName(newName);
                    } else {
                        Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateDisplayName(String newName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tvUserName.setText(newName);
                        Toast.makeText(this, "Name updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to update name", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void updateProfilePhoto(Uri imageUri) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(imageUri)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Glide.with(this).load(imageUri).into(ivProfileImage);
                        Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to update picture", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
