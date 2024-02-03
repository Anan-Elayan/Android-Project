package com.example.androidproject.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
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
import com.example.androidproject.R;
import com.example.androidproject.home.OnItemClickListener;
import com.example.androidproject.home.SpaceItemDecoration;
import com.example.androidproject.model.Course;
import com.example.androidproject.model.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TasksForCourse extends AppCompatActivity implements OnItemClickListener {
    RecyclerView recyclerViewTasks;
    TasksAdapter taskAdapter;
    public ArrayList<Task> taskList;
    private Task taskDetails;
    private TextView lblCourseid;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_for_course); // Move this line here
        taskList = new ArrayList<>();
        recyclerViewTasks = findViewById(R.id.recyclerView);

        Intent intent = getIntent();
         course = (Course) intent.getSerializableExtra("course");

        lblCourseid = findViewById(R.id.lblCourseid);
        lblCourseid.setText("Show Tasks for " + course.getCourseID());
        setupTasks();
    }

    public void AddNewTask(View view) {
        Intent intent = new Intent(TasksForCourse.this, AddTask.class);
        intent.putExtra("course",  course );
        startActivity(intent);
    }

    private void setupRecyclerView() {

        taskAdapter = new TasksAdapter(taskList);
        taskAdapter.setOnItemClickListener(this);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.setAdapter(taskAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_item_spacing);
        recyclerViewTasks.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    // عرض أيقونة سلة المحذوفات وتغيير الخلفية
                    TasksAdapter.taskViewHolder holder = (TasksAdapter.taskViewHolder) viewHolder;
                    holder.imageViewDelete.setVisibility(View.VISIBLE);
                    // عرض الإنذار للتأكيد
                    showAlertForConfirmation(viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (dX < 0) { // فقط عند السحب لليسار
                    View itemView = viewHolder.itemView;
                    // إظهار أيقونة سلة المحذوفات
                    TasksAdapter.taskViewHolder holder = (TasksAdapter.taskViewHolder) viewHolder;
                    holder.imageViewDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                TasksAdapter.taskViewHolder holder = (TasksAdapter.taskViewHolder) viewHolder;
                holder.imageViewDelete.setVisibility(View.GONE); // إخفاء أيقونة سلة المحذوفات
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewTasks);
    }

    public void setupTasks() {
        System.out.println("setupCourses");
        //String id = LoginActivity.id;
        String url = "http://10.0.2.2:5000/getTasks/" + course.getCourseID();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String taskID = obj.getString("taskID");
                        //System.out.println("the course is :"+getCourseDetails(courseId).toString());
                        getTasksDetails(taskID);


                    } catch (JSONException exception) {
                        Log.d("Error", exception.toString());
                    }
                }
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

    public void getTasksDetails(String taskID) {
        System.out.println("getCourseDetails");
        //Course courseDetails;
        String url = "http://10.0.2.2:5000/getTasksById/" + taskID;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject obj = response.getJSONObject(0);
                    String courseID = obj.getString("courseID");
                    String taskDate = obj.getString("taskDate");
                    String taskDescription = obj.getString("taskDescription");
                    int taskID = obj.getInt("taskID");
                    String taskTime = obj.getString("taskTime");
                    String taskTitle = obj.getString("taskTitle");
                    taskDetails = new Task(taskID, courseID, taskTitle, taskDescription, taskDate,taskTime);
                    taskList.add(taskDetails);
                } catch (JSONException exception) {
                    Log.d("Error", exception.toString());
                }
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

    }

    private void showAlertForConfirmation(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this course ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            String courseID = taskAdapter.getCourseAtPosition(position).getCourseID();
            String url = "http://10.0.2.2:5000/deleteCourse/" + course.getCourseID() + "/" + courseID;
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
            taskAdapter.removeItem(position);
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

    }

    public void showTasks(View view) {
        setupRecyclerView();
    }
}