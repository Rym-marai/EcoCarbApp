package com.example.ecocarb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context context;
    private ArrayList<Task> tasks;
    private String taskPath; 

    public TaskAdapter(Context context, ArrayList<Task> tasks , String taskPath){
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
        this.taskPath = taskPath;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Task task = tasks.get(position);

        TextView titleTextView = convertView.findViewById(R.id.title_textView);
        TextView descriptionTextView = convertView.findViewById(R.id.description_textView);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        titleTextView.setText(task.getTitle());
        descriptionTextView.setText(task.getDescription());

        ImageView imageView = convertView.findViewById(R.id.imageView2);
        imageView.setImageResource(task.getImageResourceId());

        // Remove the onCheckedChanged listener
        checkBox.setOnCheckedChangeListener(null);

        // Set the initial checked state of the CheckBox based on the Task object
        checkBox.setChecked(task.isChecked());

        // Add the onCheckedChanged listener
        // Add the onCheckedChanged listener
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ((ListView)parent).setItemChecked(position, isChecked);
            task.setChecked(isChecked);

            // Check the type of the context before casting
            if (context instanceof ChallengeActivity) {
                ((ChallengeActivity) context).updateProgressBar();
            } else if (context instanceof ChallengeActivity2) {
                ((ChallengeActivity2) context).updateProgressBar();
            }

            // Update the isChecked field in the Firebase Realtime Database
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the ID of the current user
            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(userId) // Make sure to replace this with the ID of the current user
                    .child(taskPath) // Use the task path
                    .child(task.getTitle()) // Use the task's title as the ID
                    .setValue(task);
        });

        return convertView;
    }
}