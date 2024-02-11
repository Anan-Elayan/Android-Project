package com.example.androidproject.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.androidproject.LoginAndRegister.LoginActivity;
import com.example.androidproject.LoginAndRegister.Register;
import com.example.androidproject.R;
import com.example.androidproject.Task.TasksForCourse;
import com.example.androidproject.addCourse.AddCourseActivity;
import com.example.androidproject.model.Course;
import com.example.androidproject.profile.ProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class HomeActivity extends AppCompatActivity implements OnItemClickListener {
    MeowBottomNavigation meowBottomNavigation;
    RecyclerView recyclerViewCourses;
    CourseAdapter courseAdapter;
    public static ArrayList<Course> courseList;
    private Course courseDetails;

    public String id = LoginActivity.id;
    public static ConstraintLayout constraintHome;
    private SharedPreferences prefs;
    TextView textViewMyCourses;
    TextView textView;
    private GifImageView load;
    private GifImageView load2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        courseList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        load = findViewById(R.id.load);
        load2 = findViewById(R.id.load2);
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        meowBottomNavigation = findViewById(R.id.butonNavegation);
        constraintHome = findViewById(R.id.constraintHome);
        textViewMyCourses = findViewById(R.id.textViewMyCourses);
        textView = findViewById(R.id.textView);
        // textView.setVisibility(View.VISIBLE);
        setupBottomNavigation();
        setupCourses();
        setupSharedPrefs();
        ColorMode();


    }


    private void setupBottomNavigation() {
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.add_circle));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.icon_material_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.account_circle));
        meowBottomNavigation.show(2, true);
        meowBottomNavigation.setOnClickMenuListener(model -> {
            Intent intent;
            switch (model.getId()) {
                case 1:
                    intent = new Intent(HomeActivity.this, AddCourseActivity.class);
                    intent.putExtra("FROM_HOME_ID_TO_ADD", id);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(HomeActivity.this, HomeActivity.class);
                    intent.putExtra("FROM_HOME_ID_TO_HOME", id);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    intent.putExtra("FROM_HOME_ID_TO_PROFILE", id);
                    startActivity(intent);
                    break;
            }
            return null;
        });
    }

    private void setupRecyclerView() {
        courseAdapter = new CourseAdapter(courseList);
        courseAdapter.setOnItemClickListener(this);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCourses.setAdapter(courseAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    CourseAdapter.CourseViewHolder holder = (CourseAdapter.CourseViewHolder) viewHolder;
                    holder.imageViewDelete.setVisibility(View.VISIBLE);
                    showAlertForConfirmation(viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (dX < 0) {
                    View itemView = viewHolder.itemView;
                    CourseAdapter.CourseViewHolder holder = (CourseAdapter.CourseViewHolder) viewHolder;
                    holder.imageViewDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                CourseAdapter.CourseViewHolder holder = (CourseAdapter.CourseViewHolder) viewHolder;
                holder.imageViewDelete.setVisibility(View.GONE);
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewCourses);
    }


    public void setupCourses() {
        System.out.println("setupCourses");
        //String id = LoginActivity.id;
        String url = "http://10.0.2.2:5000/getCourses/" + id;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String courseId = obj.getString("courseID");
                        getCourseDetails(courseId);
                        load.setVisibility(View.INVISIBLE);

                    } catch (JSONException exception) {
                        Log.d("Error", exception.toString());
                    }
                }
                if (response.length() < 1) {
                    load.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    load2.setVisibility(View.VISIBLE);
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Error_json", error.toString());
            }
        });
        queue.add(request);

        // return courseList;
    }

    public void getCourseDetails(String courseID) {
        //Course courseDetails;
        String url = "http://10.0.2.2:5000/getCoursesById/" + courseID;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject obj = response.getJSONObject(0);
                    String courseDr = obj.getString("courseDr");
                    String courseEndTime = obj.getString("courseEndTime");
                    String courseID = obj.getString("courseID");
                    String courseStartTime = obj.getString("courseStartTime");
                    String date = obj.getString("date");
                    courseDetails = new Course(courseID, courseStartTime, courseDr, date, courseEndTime);
                    System.out.println("first ID ----> " + courseDetails.getCourseID());
                    courseList.add(courseDetails);
                    setupRecyclerView();

                } catch (JSONException exception) {
                    Log.d("Error", exception.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(HomeActivity.this, error.toString(),
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
            String courseID = courseAdapter.getCourseAtPosition(position).getCourseID();
            String url = "http://10.0.2.2:5000/deleteCourse/" + id + "/" + courseID;
            RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);

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
                                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
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
            courseAdapter.removeItem(position);
        });

        builder.setNegativeButton("No-", (dialog, which) -> {
            // Do nothing or provide any specific action if needed
            // This will cancel the deletion operation
            courseAdapter.notifyItemChanged(position);
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick(int position) {
        Course selectedCourse = courseList.get(position);
        Intent intent = new Intent(HomeActivity.this, TasksForCourse.class);
        intent.putExtra("course", selectedCourse);
        startActivity(intent);
    }

    public void action(View view) {
        //setupRecyclerView();
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }


    private void ColorMode() {
        boolean dark_mode = prefs.getBoolean("DARK MODE", false);
        if (dark_mode) {
            constraintHome.setBackgroundColor(getResources().getColor(R.color.blackModeColor));
            textViewMyCourses.setTextColor(getResources().getColor(R.color.white));
            textView.setTextColor(getResources().getColor(R.color.white));
        }
    }
}