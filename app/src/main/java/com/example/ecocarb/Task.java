package com.example.ecocarb;

public class Task {
    private String title;
    private String description;
    private int imageResourceId;
    private boolean isChecked;

    // No-argument constructor
    public Task() {}

    // New three-argument constructor
    public Task(String title, String description, int imageResourceId) {
        this.title = title;
        this.description = description;
        this.imageResourceId = imageResourceId;
        this.isChecked = false; // Set isChecked to false by default
    }

    public Task(String title, String description, int imageResourceId, boolean isChecked) {
        this.title = title;
        this.description = description;
        this.imageResourceId = imageResourceId;
        this.isChecked = isChecked; }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }



    // This method is used by ArrayAdapter to convert a Task object into a String
    @Override
    public String toString() {
        return title + "\n" + description;
    }
}






