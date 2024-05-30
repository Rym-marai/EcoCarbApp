package com.example.ecocarb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;

    private TextView signupRedirectText, forgotPasswordText;

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.login_email);
        forgotPasswordText = findViewById(R.id.forgot_password);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signUpRedirectText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = loginEmail.getText().toString().trim();
                final String pass = loginPassword.getText().toString().trim();

                if (user.isEmpty()){
                    loginEmail.setError("Email cannot be empty");
                    return;
                }
                if (pass.isEmpty()){
                    loginPassword.setError("Password cannot be empty");
                    return;
                }

                // Retrieve the user info from the Realtime Database
                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .orderByChild("email")
                        .equalTo(user)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // User exists, check password
                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        if (userSnapshot.child("password").getValue(String.class).equals(pass)) {
                                            String username = userSnapshot.child("username").getValue(String.class);
                                            if (username != null) {
                                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                                // Get the user's ID
                                                String userId = userSnapshot.getKey();

                                                // Fetch the score from the correct node
                                                // After successful login
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("users")
                                                        .child(userId)
                                                        .child("score")
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot scoreSnapshot) {
                                                                // Pass the user's ID to WelcActivity
                                                                Integer score = scoreSnapshot.getValue(Integer.class);
                                                                Intent intent = new Intent(LoginActivity.this, WelcActivity.class);
                                                                intent.putExtra("username", username); // Pass the username to WelcActivity
                                                                intent.putExtra("userId", userId);
                                                                if (score != null) {
                                                                    intent.putExtra("SCORE", score); // Pass the score to WelcActivity if it exists
                                                                }
                                                                Log.d("LoginActivity", "onDataChange: starting WelcActivity");
                                                                startActivity(intent);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                Toast.makeText(LoginActivity.this, "Failed to fetch score: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Username is null", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(LoginActivity.this, "Failed to fetch user: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = loginEmail.getText().toString().trim();

                if (emailAddress.isEmpty()){
                    loginEmail.setError("Email cannot be empty");
                    return;
                }

                FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Password reset email sent to " + emailAddress, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

}