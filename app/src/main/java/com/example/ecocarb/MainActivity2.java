package com.example.ecocarb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent; // Add this line
import android.os.Bundle;
import android.view.View;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Find the ConstraintLayout using its ID
        ConstraintLayout constraintLayout = findViewById(R.id.ch1);
        ConstraintLayout constraintLayout2 = findViewById(R.id.ch2);

        // Set an OnClickListener for the ConstraintLayout
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new Intent to start ChallengeActivity
                Intent intent = new Intent(MainActivity2.this, ChallengeActivity.class);

                // Put extra data into the Intent
                intent.putExtra("TASK_TYPE", "taskType1");

                // Start ChallengeActivity
                startActivity(intent);
            }
        });

        constraintLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new Intent to start ChallengeActivity
                Intent intent = new Intent(MainActivity2.this, ChallengeActivity2.class);

                // Put extra data into the Intent
                intent.putExtra("TASK_TYPE", "taskType1");

                // Start ChallengeActivity
                startActivity(intent);
            }
        });
    }
}