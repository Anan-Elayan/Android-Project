package com.example.androidproject.Task;

import android.database.DataSetObserver;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import pl.droidsonroids.gif.GifImageView;


public class TasksForCourse extends AppCompatActivity implements OnItemClickListener  {


    ListView listViewTasks;
    TaskListAdapter listAdapter;
    public ArrayList<Task> taskList;
    private TextView lblCourseid;
    Course course;
    ConstraintLayout constraintLayout;
    private SharedPreferences prefs;

    TextView textView;
    private GifImageView load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("First");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_for_course);
        textView = findViewById(R.id.textView);
        load=findViewById(R.id.load);
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
        textView.setVisibility(View.INVISIBLE);
        setupTasks();


    }

    public void AddNewTask(View view) {
        Intent intent = new Intent(TasksForCourse.this, AddTask.class);
        intent.putExtra("course",  course );
        startActivity(intent);
    }

    private void setupListView() {
        System.out.println("First");
        listAdapter = new TaskListAdapter(TasksForCourse.this,taskList);
        listViewTasks.setAdapter(listAdapter);
        System.out.println("second");

        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = taskList.get(position);
                Intent intent = new Intent(TasksForCourse.this, TaskDetails.class);
                System.out.println("taskID 1"+selectedTask.getTaskID());
                intent.putExtra("taskID", selectedTask.getTaskID());
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
                        load.setVisibility(View.INVISIBLE);
                        taskList.add(task);

                    } catch (JSONException exception) {
                        Log.d("Error", exception.toString());
                    }
                }
                if(response.length()<1)
                    textView.setVisibility(View.VISIBLE);

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


    @Override
    public void onItemClick(int position) {
        Task selectedCourse = taskList.get(position);
        Intent intent = new Intent(TasksForCourse.this, TaskDetails.class);
        intent.putExtra("task", selectedCourse);
        startActivity(intent);
    }

    public void showTasks(View view) {
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