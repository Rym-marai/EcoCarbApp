package com.example.ecocarb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the Intent that started this activity and extract the username
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String userId = intent.getStringExtra("userId");
        Log.d("WelcActivity", "onCreate: received username = " + username);

        if (username == null) {
            // Handle case where username is null
            // For example, finish the activity and show a toast message
            Toast.makeText(WelcActivity.this, "Error: username is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView5);

        // ... other code ...

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId)
                .child("score")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot scoreSnapshot) {
                        Integer score = scoreSnapshot.getValue(Integer.class);
                        String welcomeMessage;
                        Button button = findViewById(R.id.startbtn);
                        if (score != null && score != 0) {
                            // The user's score exists and is not 0, they have logged in before
                            welcomeMessage = "Welcome back," + username + " \n Continue your journey of being an eco-friendly person \n Your test  " + score + " %";
                            SpannableString spannable = new SpannableString(welcomeMessage);
                            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#41B06E")), 13, 13 + username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            textView.setText(spannable);                            button.setText("Challenges");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(WelcActivity.this, MainActivity2.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            // The user's score does not exist or is 0, they are a new user
                            welcomeMessage = "Welcome " + username + " to EcoCarb. Your Hub for Eco-Friendly Living Tips and Green Solutions";
                            SpannableString spannable = new SpannableString(welcomeMessage);
                            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#41B06E")), 8, 8 + username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            textView.setText(spannable);                            button.setText("Explore Your Ecological Impact");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(WelcActivity.this, MainActivity3.class);
                                    startActivity(intent);
                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(WelcActivity.this, "Failed to fetch score: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}