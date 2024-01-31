package com.example.androidproject.Task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.androidproject.R;


public class TasksForCourse extends AppCompatActivity {
    private String courseID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_for_course);

        Intent intent = getIntent();
        if (intent != null) {
            courseID = intent.getStringExtra("courseID");
        }
    }

    public void AddNewTask(View view) {
        Intent intent = new Intent(TasksForCourse.this, AddTask.class);
        intent.putExtra("courseID",courseID);
        startActivity(intent);
    }
}