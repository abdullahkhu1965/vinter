package com.example.vinter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vinter.R;
// Import Firebase classes
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    // Firebase Authentication instance
    private FirebaseAuth auth;
    // UI elements from activity_sign_in.xml (Note: IDs are 'edt' for this XML)
    private EditText edtEmail, edtPassword;
    private Button btnSignIn;
    private TextView txtSignupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link the activity to its layout
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Map UI elements to their IDs in the XML
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtSignupLink = findViewById(R.id.txtSignupLink);

        // 1. Set listener for the Sign In Button
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                // Basic Input Validation
                if (email.isEmpty()) {
                    edtEmail.setError("Email cannot be empty");
                    return;
                }
                if (password.isEmpty()) {
                    edtPassword.setError("Password cannot be empty");
                    return;
                }

                // Log in user with email and password
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignIn.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                            // Navigate to the main application activity
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                            finish(); // Close the login activity
                        } else {
                            // Login failed
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Authentication Failed";
                            Toast.makeText(SignIn.this, "Login Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // 2. Set listener for the "Sign Up" link to redirect
        txtSignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });
    }
}