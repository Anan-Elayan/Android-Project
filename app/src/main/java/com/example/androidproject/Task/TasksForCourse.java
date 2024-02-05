package com.example.androidproject.Task;

import android.database.DataSetObserver;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.LoginAndRegister.LoginActivity;
import com.example.androidproject.R;
import com.example.androidproject.home.OnItemClickListener;
import com.example.androidproject.model.Course;
import com.example.androidproject.model.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TasksForCourse extends AppCompatActivity implements OnItemClickListener  {
    //    RecyclerView recyclerViewTasks;
    ListView listViewTasks;

    //    TasksAdapter adapter;
    ListAdapter listAdapter;
    public ArrayList<Task> taskList;
    private Task taskDetails;
    private TextView lblCourseid;
    Course course;

    ConstraintLayout constraintLayout;

    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_for_course); // Move this line here
        taskList = new ArrayList<>();

        listViewTasks = findViewById(R.id.listViewTasks);

        constraintLayout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();
        course = (Course) intent.getSerializableExtra("course");

        lblCourseid = findViewById(R.id.lblCourseid);
        lblCourseid.setText("Tasks for " + course.getCourseID());
        setupSharedPrefs();
        ColorMode();

    }

    @Override
    protected void onStart(){
        super.onStart();
        setupTasks();
        // setupRecyclerView();

    }

    public void AddNewTask(View view) {
        Intent intent = new Intent(TasksForCourse.this, AddTask.class);
        intent.putExtra("course",  course );
        startActivity(intent);
    }

    private void setupListView() {
        listAdapter = new ListAdapter(this,R.layout.list_row,taskList);
        listViewTasks.setAdapter(listAdapter);
        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = taskList.get(position);
                Intent intent = new Intent(TasksForCourse.this, TaskDetails.class);
                intent.putExtra("task", selectedTask);
                startActivity(intent);
            }
        });
    }


    public void setupTasks() {
        taskList = new ArrayList<>();
        System.out.println("setupCourses");
        //String id = LoginActivity.id;

        String url = "http://10.0.2.2:5000/getTasksById/" +"/"+ LoginActivity.id+"/"+course.getCourseID();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String studentID = obj.getString("studentID");
                        String CourseID = obj.getString("CourseID");
                        String taskDate = obj.getString("taskDate");
                        String taskDescription = obj.getString("taskDescription");
                        String taskTime = obj.getString("taskTime");
                        String taskTitle = obj.getString("taskTitle");
                        String taskID = obj.getString("taskID");

                        Task task = new Task(studentID,CourseID,taskTitle, taskDescription,taskDate,taskTime,taskID);
                        System.out.println("task--> " + task.toString());

                        taskList.add(task);

                    } catch (JSONException exception) {
                        Log.d("Error", exception.toString());
                    }
                }
                setupListView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TasksForCourse.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Error_json", error.toString());
            }
        });
        queue.add(request);

        // return courseList;
    }


    private void showAlertForConfirmation(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this course ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            String taskID = listAdapter.getItemByPosition(position).getTaskID();
            String url = "http://10.0.2.2:5000/deleteTask/" + taskID;
            RequestQueue queue = Volley.newRequestQueue(TasksForCourse.this);

            // Create a JsonObjectRequest with PUT method
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.DELETE,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Handle the response as needed
                                String message = response.getString("message");
                                Toast.makeText(TasksForCourse.this, message, Toast.LENGTH_SHORT).show();
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
//            listAdapter.removeItem(position);
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing or provide any specific action if needed
            // This will cancel the deletion operation
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick(int position) {
        Task selectedCourse = taskList.get(position);
        Intent intent = new Intent(TasksForCourse.this, TaskDetails.class);
        intent.putExtra("task", selectedCourse);
        startActivity(intent);
    }

    public void showTasks(View view) {
        // setupRecyclerView();
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void ColorMode(){
        boolean dark_mode = prefs.getBoolean("DARK MODE",false);
        if(dark_mode){
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.blackModeColor));
            lblCourseid.setTextColor(getResources().getColor(R.color.white));

        }
    }

}