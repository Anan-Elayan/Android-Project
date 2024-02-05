package com.example.androidproject.Task;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.R;
import com.example.androidproject.model.Task;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

public class TaskDetails extends AppCompatActivity {


    Task task;

    TextInputEditText txtTitle;
    TextInputEditText txtDescription;
    TextInputEditText txtDate;
    TextInputEditText txtTime;
    ImageButton btnUpdateTask;
    private ImageButton btnClock;
    private ImageButton btnCalender;
    private TextView lblCourse;
    private SharedPreferences prefs;
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        Intent intent = getIntent();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            task = intent.getSerializableExtra("task", Task.class);
        }
        setupViews();
        btnClock.setOnClickListener(new View.OnClickListener() {
            int hour = 0, minutes = 0;

            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String amPm;
                        if (selectedHour >= 12) {
                            amPm = "PM";
                            selectedHour -= 12;
                        } else {
                            amPm = "AM";
                        }
                        // Handle midnight (12:00 AM) and noon (12:00 PM)
                        if (selectedHour == 0) {
                            selectedHour = 12;
                        }
                        hour = selectedHour;
                        minutes = selectedMinute;
                        txtTime.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minutes, amPm));
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(TaskDetails.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, onTimeSetListener, hour, minutes, false);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });
        btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TaskDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Month is zero-based, so we add 1 to it
                                monthOfYear += 1;
                                // Display selected date in the TextInputEditText
                                txtDate.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", monthOfYear, dayOfMonth, year));
                            }
                        },
                        year, // Initial year
                        month, // Initial month
                        day // Initial day
                );
                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        setData();

        setupSharedPrefs();
        ColorMode();
    }


    public void setupViews() {
        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtDate = findViewById(R.id.txtdate);
        txtTime = findViewById(R.id.txtTime);
        btnClock = findViewById(R.id.btnClock);
        btnUpdateTask = findViewById(R.id.btnUpdateTask);
        btnCalender = findViewById(R.id.btnCalender);
        lblCourse = findViewById(R.id.lblCourse);
        constraintLayout = findViewById(R.id.constraintLayout);
    }

    public void setData() {
        txtTitle.setText(task.getTaskTitle());
        txtDescription.setText(task.getTaskDescription());
        txtDate.setText(task.getTaskDate());
        txtTime.setText(task.getTaskTime());
        lblCourse.setText(task.getCourseID());
    }


    public void actionUpdate(View view) {
        String url = "http://10.0.2.2:5000/updateTasks/" + task.getCourseID() + "/" + txtTitle.getText().toString() + "/" +
                txtDescription.getText().toString() + "/" + txtTime.getText().toString() + "/" + txtDate.getText().toString() + "/"
                + task.getStudentID() + "/" + task.getTaskID();

        RequestQueue queue = Volley.newRequestQueue(TaskDetails.this);

        // Create a JsonObjectRequest with PUT method
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Handle the response as needed
                            String message = response.getString("message");
                            Toast.makeText(TaskDetails.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                    }
                }
        );
        queue.add(request);
    }


    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }


    private void ColorMode() {
        boolean dark_mode = prefs.getBoolean("DARK MODE", false);
        if (dark_mode) {
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.blackModeColor));
            lblCourse.setTextColor(getResources().getColor(R.color.white));
            txtTitle.setTextColor(getResources().getColor(R.color.white));
            txtTitle.setHintTextColor(getResources().getColor(R.color.white));

            txtDescription.setTextColor(getResources().getColor(R.color.white));
            txtDescription.setHintTextColor(getResources().getColor(R.color.white));


            txtDate.setTextColor(getResources().getColor(R.color.white));
            txtDate.setHintTextColor(getResources().getColor(R.color.white));

            txtTime.setTextColor(getResources().getColor(R.color.white));
            txtTime.setHintTextColor(getResources().getColor(R.color.white));

            btnCalender.setImageResource(R.drawable.light_calender);
            btnClock.setImageResource(R.drawable.light_clock);




        }
    }
}