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

public class ChallengeActivity2 extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Task> adapter;
    private ArrayList<Task> taskList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge2);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);

        // Predefined list of tasks
        Task[] predefinedTasks = {
                new Task("Bike to Work", " Commit to biking to work or school instead of using a car for at least three days during the week.", R.drawable.aa1, false),
                new Task("Public Transportation Day", "Use public transportation such as buses, trains, or subways instead of driving your car for a whole day", R.drawable.aa2, false),
                new Task("Carpooling Initiative", "Organize or join a carpool with coworkers, friends, or neighbors for commuting to reduce the number of vehicles on the road.", R.drawable.aa3, false),
                new Task("Walkable Errands", "Plan to walk or use a bicycle for errands within a certain radius from your home instead of driving.", R.drawable.aa4, false),
                new Task("Electric Vehicle Test Drive", "Explore the option of electric vehicles by scheduling a test drive at a local dealership or rental service.\n", R.drawable.aa5, false),
                new Task("Promote Eco-friendly Transport", "Share information about eco-friendly transportation options with friends, family, or on social media platforms.", R.drawable.aa6, false),
                new Task("Reduce Air Travel", "Limit air travel by opting for video conferences or choosing destinations that can be reached by train or bus. ", R.drawable.aa7, false),
                new Task("Optimize Driving Routes", "Use apps or tools to optimize driving routes to reduce fuel consumption and emissions. ", R.drawable.aa8, false),
                new Task("Support Sustainable Transport Initiatives", "Support initiatives in your community that promote cycling lanes, pedestrian-friendly infrastructure, or public transportation improvements. ", R.drawable.aa9, false),
                new Task("Calculate Carbon Footprint", "Calculate your carbon footprint from transportation activities using online calculators and pledge to reduce it over time by adopting eco-friendly transportation practices.", R.drawable.aa10, false),
                // ...
        };

        taskList = new ArrayList<>();
        Collections.addAll(taskList, predefinedTasks);

        adapter = new TaskAdapter(this, taskList, "tasks2"); // For ChallengeActivity2
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
                .child("tasks2")
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
                        Toast.makeText(ChallengeActivity2.this, "Failed to fetch tasks: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(ChallengeActivity2.this, MainActivity6.class);
            startActivity(intent);
        }
    }

    private void displayTasks(String taskType) {
        // Display the tasks based on the taskType
        // ...
    }

}