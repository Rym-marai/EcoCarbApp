package com.example.ecocarb;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ChallengeActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Task> adapter;
    private ArrayList<Task> taskList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);

        // Predefined list of tasks
        Task[] predefinedTasks = {
                new Task("Bring Your Own Bags", " Carry reusable bags when shopping for groceries or other items to avoid using plastic bags.", R.drawable.c3, false),
                new Task("Say No to Plastic Straws", "Refuse plastic straws when ordering drinks at restaurants or cafes. Use alternatives like metal or bamboo straws if needed.", R.drawable.c9, false),
                new Task("Use Reusable Water Bottles", "Carry a reusable water bottle with you and refill it instead of buying bottled water.", R.drawable.c7, false),
                new Task("Choose Plastic-Free Packaging", "Opt for products with minimal or no plastic packaging. Look for items in glass, paper, or cardboard containers", R.drawable.c1, false),
                new Task("Bring Your Own Container", "Bring your own containers for takeout food or leftovers to avoid using single-use plastic containers.", R.drawable.c10, false),
                new Task("Switch to Plastic-Free Toiletries", "Use shampoo bars, bar soap, and other plastic-free alternatives for personal care products.", R.drawable.c5, false),
                new Task("Avoid Plastic Utensils", "Bring your own reusable utensils when eating out or ordering takeout food to avoid plastic cutlery.", R.drawable.c4, false),
                new Task("Shop at Farmers' Markets", "Buy fresh produce from farmers' markets or local farms to reduce plastic packaging used in supermarkets.", R.drawable.c6, false),
                new Task("Make Homemade Snacks", " Prepare homemade snacks and meals using ingredients that are not packaged in plastic.", R.drawable.c2, false),
                new Task("Spread Awareness", "Share your plastic-free journey on social media and encourage others to join the challenge. Raise awareness about the environmental impact of plastic pollution.", R.drawable.c10, false),
                // ...
        };

        taskList = new ArrayList<>();
        Collections.addAll(taskList, predefinedTasks);

        adapter = new TaskAdapter(this, taskList, "tasks"); // For ChallengeActivity
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            updateProgressBar();
        });

        // Call updateProgressBar after setting the adapter
        updateProgressBar();

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Get the extra data from the Intent
        String taskType = intent.getStringExtra("TASK_TYPE");

        // Display the tasks based on the taskType
        displayTasks(taskType);


        loadTasks();
    }

    private void loadTasks() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the ID of the current user

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userId)
                .child("tasks")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                            Task task = taskSnapshot.getValue(Task.class);

                            // Find the task in the taskList that has the same title as the task from Firebase
                            int index = -1;
                            for (int i = 0; i < taskList.size(); i++) {
                                if (taskList.get(i).getTitle().equals(task.getTitle())) {
                                    index = i;
                                    break;
                                }
                            }

                            if (index == -1) {
                                // The task is not in the taskList, so add it
                                taskList.add(task);
                                index = taskList.size() - 1;
                            } else {
                                // The task is already in the taskList, so update its isChecked status
                                taskList.get(index).setChecked(task.isChecked());
                            }

                            // Set the checked status of the item in the ListView based on the isChecked field of the Task
                            listView.setItemChecked(index, task.isChecked());
                        }
                        adapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
                        updateProgressBar(); // Update the progress bar after loading tasks
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ChallengeActivity.this, "Failed to fetch tasks: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }




    public void updateProgressBar() {
        int totalTasks = taskList.size();
        int completedTasks = 0;

        // Calculate the number of completed tasks
        for (Task task : taskList) {
            if (task.isChecked()) {
                completedTasks++;
            }
        }

        // Calculate the progress as a percentage
        int progress = totalTasks == 0 ? 0 : (completedTasks * 100) / totalTasks;

        // Set the progress on the progress bar
        progressBar.setProgress(progress);

        if (progress == 100) {
            // All tasks completed
            Intent intent = new Intent(ChallengeActivity.this, MainActivity6.class);
            startActivity(intent);
        }
    }

    private void displayTasks(String taskType) {
        // Display the tasks based on the taskType
        // ...
    }

}