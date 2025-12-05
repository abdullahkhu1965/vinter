package com.example.vinter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper; // Looper import is good practice for newer Android versions

import androidx.appcompat.app.AppCompatActivity;

import com.example.vinter.R;
// Import Firebase classes for authentication check
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// NOTE: Renamed class to Splash to match your manifest and code structure
public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds delay, matching your original code
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Use a Handler to execute code after the splash delay
        // Using Looper.getMainLooper() is recommended for new Handler calls
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 1. Check if user is currently signed in
                FirebaseUser currentUser = auth.getCurrentUser();

                Intent intent;
                if (currentUser != null) {
                    // 2. User is signed in, go directly to the Main content
                    intent = new Intent(Splash.this, MainActivity.class);
                } else {
                    // 3. User is NOT signed in, go to the Sign In screen
                    intent = new Intent(Splash.this, SignIn.class);
                }

                startActivity(intent);
                finish(); // Close the Splash activity
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}