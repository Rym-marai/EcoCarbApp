package com.example.ecocarb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity4 extends AppCompatActivity {

    private TextView scoreTextView;
    private Button backButton;
    private Button redobtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        scoreTextView = findViewById(R.id.score_text_view);
        backButton = findViewById(R.id.back_button);
        redobtn = findViewById(R.id.redobtn);

        // Retrieve the score percentage from the intent
        int scorePercentage = getIntent().getIntExtra("SCORE", 0);

        // Determine the performance level based on the score percentage
        String feedback;
        if (scorePercentage <= 30) {
            feedback = scorePercentage + "% \n \n Your ecological impact is concerning. Consider making significant changes to reduce your footprint";
        } else if (scorePercentage <= 60) {
            feedback = scorePercentage + "% \n \n You're making some efforts, but there's room for improvement to reduce your ecological impact.";
        } else {
            feedback = scorePercentage + "% \n \n  Your ecological impact is commendable. Keep up the good work!";
        }

        // Display the feedback
        scoreTextView.setText(feedback);

        // Set onClickListener for the backButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity2
                Intent intent = new Intent(MainActivity4.this, MainActivity2.class);
                startActivity(intent);
            }
        });



        redobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4.this, MainActivity3.class);
                startActivity(intent);
            }
        });
    }
    }
