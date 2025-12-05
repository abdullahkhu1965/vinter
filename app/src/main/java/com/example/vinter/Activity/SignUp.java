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

public class SignUp extends AppCompatActivity {

    // Firebase Authentication instance
    private FirebaseAuth auth;
    // UI elements from activity_sign_up.xml
    private EditText editName, editEmail, editPassword;
    private Button btnSignUp;
    private TextView txtSigninLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link the activity to its layout
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Map UI elements to their IDs in the XML
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtSigninLink = findViewById(R.id.txtSigninLink);

        // 1. Set listener for the Sign Up Button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String name = editName.getText().toString().trim(); // Name field included

                // Basic Input Validation
                if (name.isEmpty()){
                    editName.setError("Full Name cannot be empty");
                    return;
                }
                if (email.isEmpty()) {
                    editEmail.setError("Email cannot be empty");
                    return;
                }
                if (password.isEmpty()) {
                    editPassword.setError("Password cannot be empty");
                    return;
                }
                if (password.length() < 6) {
                    editPassword.setError("Password must be at least 6 characters long");
                    return;
                }

                // Create user with email and password
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                            // Navigate to Sign In screen after successful registration
                            startActivity(new Intent(SignUp.this, SignIn.class));
                            finish(); // Prevent user from going back to sign up screen
                        } else {
                            // Registration failed (e.g., email already in use, weak password)
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(SignUp.this, "Registration Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // 2. Set listener for the "Sign In" link to redirect
        txtSigninLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, SignIn.class));
            }
        });
    }
}