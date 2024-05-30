package com.example.ecocarb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity3 extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private Button nextButton;
    private TextView scoreTextView;
    private int currentQuestionIndex = 0;
    private int[] userAnswers;
    private Question[] questions;

    private DatabaseReference mDatabase;
    private String userId; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Initialize UI elements
        questionTextView = findViewById(R.id.question_text_view);
        radioGroup = findViewById(R.id.radio_group);
        radioButton1 = findViewById(R.id.radio_button1);
        radioButton2 = findViewById(R.id.radio_button2);
        radioButton3 = findViewById(R.id.radio_button3);
        radioButton4 = findViewById(R.id.radio_button4);
        nextButton = findViewById(R.id.next_button);
        scoreTextView = findViewById(R.id.score_text_view);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        // Initialize questions
        questions = new Question[]{
                new Question("Which best describes your diet?",
                        new String[]{"Vegan", "Vegetarian", "Pescetarian", "Try to eat less meat",}),
                new Question("How would you describe your flying habits in a typical, average year?",
                        new String[]{"I fly rarely", "Occasionally", "Regularly" , "Never fly"}),
                new Question("How much do you get around by car annually?",
                        new String[]{"I don't drive or ride", "Up to 5,000 km", "5,000 - 10,000 km", "10,000-15,000 km"}),
                new Question("What kind of fuel does your car use?",
                        new String[]{"Electric (green energy)", "Electric", "Natural gas", "Gasoline, diesel, or hybrid"}),
                new Question("How much do you shop?",
                        new String[]{"Rarely", "Average Shopper", "Shopper", "Luxury shopper"}),
                new Question("How big is your home?",
                        new String[]{"Studio", "One-bedroom", "Two-bedroom", "Three-bedroom"}),
                new Question("What is your primary mode of transportation for daily commuting?",
                        new String[]{"Public transportation", "Walking or cycling", "Carpooling", "Personal vehicle"}),
                new Question("Are you actively involved in any environmental or sustainability initiatives in your community?",
                        new String[]{"Yes, regularly" ,"Yes,occasionally" ,"No, but I'm interested" ,"No, and I'm not intereste"})};


        // Initialize user answers array
        userAnswers = new int[questions.length];

        // Display first question
        displayQuestion();

        // Next button click listener
        // Next button click listener
        // Next button click listener
        // ... other code ...

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if user has selected an answer
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity3.this, "Please select an answer.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save user's answer
                userAnswers[currentQuestionIndex] = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId())) + 1;

                // Move to next question or display score if it's the last question
                if (currentQuestionIndex < questions.length - 1) {
                    currentQuestionIndex++;
                    displayQuestion();
                } else {
                    int score = calculateScore();

                    // Save the score to Firebase
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference userRef = mDatabase.child("users").child(userId);
                    userRef.child("score").setValue(score);

                    Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
                    intent.putExtra("SCORE", score); // Pass the score to MainActivity4
                    startActivity(intent);
                }
            }
        });

// ... other code ...
    }



    private void displayQuestion() {
        // Afficher le texte de la question
        questionTextView.setText(questions[currentQuestionIndex].getQuestion());
        String[] choices = questions[currentQuestionIndex].getChoices();

        // Assurez-vous que le tableau choices contient au moins 4 éléments
        if (choices.length >= 4) {
            radioButton1.setText(choices[0]);
            radioButton2.setText(choices[1]);
            radioButton3.setText(choices[2]);
            radioButton4.setText(choices[3]);
        } else {
            // Gérer le cas où le tableau n'a pas suffisamment d'éléments
            // Ici, vous pouvez par exemple cacher les boutons radio ou afficher un message d'erreur
        }

        // Désélectionner tous les boutons radio
        radioGroup.clearCheck();
    }

    private int calculateScore() {
        int totalScore = 0;
        int[][] scores = {
                {10, 7, 4, 1}, // Scores for question 1
                {1, 4, 7, 10}, // Scores for question 2
                {10, 7, 4, 1}, // Scores for question 3
                {10, 7, 4, 1}, // Scores for question 4
                {1, 4, 7, 10}, // Scores for question 5
                {1, 4, 7, 10}, // Scores for question 6
                {10, 7, 4, 1}, // Scores for question 7
                {10, 7, 4, 1}  // Scores for question 8
        };

        for (int i = 0; i < userAnswers.length; i++) {
            int answerIndex = userAnswers[i] - 1;
            if (answerIndex >= 0 && answerIndex < scores[i].length) {
                totalScore += scores[i][answerIndex];
            }
        }

        // Calculate the maximum possible score
        int maxScore = userAnswers.length * 10; // Assuming the maximum score for each question is 10

        // Calculate and return the percentage
        return (totalScore * 100) / maxScore;
    }


    private int getMax(int[] arr) {
        int max = arr[0];
        for (int value : arr) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}

class Question {
    private String question;
    private String[] choices;

    public Question(String question, String[] choices) {
        this.question = question;
        this.choices = choices;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getChoices() {
        return choices;
    }
}
