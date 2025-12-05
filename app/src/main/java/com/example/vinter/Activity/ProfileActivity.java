package com.example.vinter.Activity; // Using your package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vinter.R; // Using your resource path
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// Assuming BaseActivity is not strictly necessary for ProfileActivity
// If it contains essential setup (like database connection), you can extend it.
public class ProfileActivity extends AppCompatActivity {

    private TextView txtUserEmail;
    private Button btnLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure your layout file is named 'activity_profile.xml'
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 1. Initialize Views
        txtUserEmail = findViewById(R.id.txtUserEmail);
        btnLogout = findViewById(R.id.btnLogout);

        // 2. Load and Display User Data
        loadUserDetails();

        // 3. Set up Logout functionality
        btnLogout.setOnClickListener(v -> logoutUser());

        // 4. Set listeners for the navigation options
        setOptionListeners();
    }

    /**
     * Retrieves the current logged-in Firebase user's details and displays them.
     */
    private void loadUserDetails() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String email = currentUser.getEmail();
            // Display the email
            txtUserEmail.setText(email != null ? email : "User Email Not Available");
        } else {
            // User shouldn't be here without a session. Force sign-in redirect.
            Toast.makeText(this, "Session expired. Redirecting to Sign In.", Toast.LENGTH_LONG).show();
            navigateToSignIn();
        }
    }

    /**
     * Signs out the user using Firebase Auth and redirects to the SignIn activity.
     */
    private void logoutUser() {
        // 1. Sign out the user from Firebase
        mAuth.signOut();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // 2. Redirect to the SignIn Activity
        navigateToSignIn();
    }

    /**
     * Handles navigation to the SignIn screen and clears the activity stack.
     */
    private void navigateToSignIn() {
        // NOTE: Ensure your SignIn activity class is named 'SignIn'
        Intent intent = new Intent(ProfileActivity.this, SignIn.class);
        // These flags prevent the user from returning to the MainActivity via the back button
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Finish the current ProfileActivity
    }

    /**
     * Sets click listeners for the list-style option buttons.
     * Replace the Toasts with actual Intent calls to new activities.
     */
    private void setOptionListeners() {
        // Order History
        findViewById(R.id.btnOrderHistory).setOnClickListener(v -> {
            Toast.makeText(this, "Order History Clicked", Toast.LENGTH_SHORT).show();
            // TODO: startActivity(new Intent(this, OrderHistoryActivity.class));
        });

        // App Settings
        findViewById(R.id.btnSettings).setOnClickListener(v -> {
            Toast.makeText(this, "App Settings Clicked", Toast.LENGTH_SHORT).show();
            // TODO: startActivity(new Intent(this, SettingsActivity.class));
        });

        // Help & Support
        findViewById(R.id.btnHelp).setOnClickListener(v -> {
            Toast.makeText(this, "Help & Support Clicked", Toast.LENGTH_SHORT).show();
            // TODO: startActivity(new Intent(this, HelpActivity.class));
        });
    }
}