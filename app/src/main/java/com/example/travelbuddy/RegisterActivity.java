package com.example.travelbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, password, fullName;
    private FirebaseAuth mAuth;
    private Button createAccountBtn;
    private DatabaseReference mDatabase;  // Firebase Realtime Database reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(); // initialize database reference

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        fullName = findViewById(R.id.fullName);
        createAccountBtn = findViewById(R.id.createAccountBtn);

        TextView loginText = findViewById(R.id.loginText);
        loginText.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        createAccountBtn.setOnClickListener(v -> {
            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();
            String fullNameText = fullName.getText().toString().trim();

            if (emailText.isEmpty() || passwordText.isEmpty() || fullNameText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                String userId = user.getUid();

                                // Save fullName to Realtime Database
                                mDatabase.child("users").child(userId).child("fullName").setValue(fullNameText)
                                        .addOnCompleteListener(dbTask -> {
                                            if (dbTask.isSuccessful()) {

                                                // Update FirebaseUser profile display name
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(fullNameText)
                                                        .build();

                                                user.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(updateTask -> {
                                                            if (updateTask.isSuccessful()) {
                                                                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(this, MainActivity.class));
                                                                finish();
                                                            } else {
                                                                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            } else {
                                                Toast.makeText(this, "Failed to save username", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                        } else {
                            Toast.makeText(this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
