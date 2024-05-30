package com.example.ecocarb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private UserDbHelper userDbHelper;
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupUsername;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        userDbHelper = new UserDbHelper(this);
        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupUsername = findViewById(R.id.signup_username);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userEmail = signupEmail.getText().toString().trim();
                final String pass = signupPassword.getText().toString().trim();
                final String username = signupUsername.getText().toString().trim();

                if (userEmail.isEmpty() || !userEmail.endsWith("@gmail.com")) {
                    signupEmail.setError("Valid Gmail address required");
                    return;
                }

                if (username.isEmpty() || username.length() < 3 || !username.matches("[a-zA-Z]*")) {
                    signupUsername.setError("Username must be at least 3 characters and contain only letters");
                    return;
                }

                if (pass.isEmpty() || pass.length() < 8) {
                    signupPassword.setError("Password must be at least 8 characters");
                    return;
                } else {
                    final User newUser = new User(username, userEmail, pass, 0);

                    SQLiteDatabase db = userDbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("username", username);
                    values.put("password", pass);
                    values.put("email", userEmail);
                    long newRowId = db.insert("users", null, values);
                    if (newRowId == -1) {
                        Toast.makeText(SignupActivity.this, "Error saving user to SQLite database", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignupActivity.this, "User saved to SQLite database", Toast.LENGTH_SHORT).show();
                    }

                    auth.createUserWithEmailAndPassword(userEmail, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        if (firebaseUser != null) {
                                            String userId = firebaseUser.getUid();
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("users")
                                                    .child(userId)
                                                    .setValue(newUser)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(SignupActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                            } else {
                                                                Toast.makeText(SignupActivity.this, "SignUp Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }
}