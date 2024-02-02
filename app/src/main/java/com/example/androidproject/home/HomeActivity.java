package com.example.androidproject.home;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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

public class HomeActivity extends AppCompatActivity implements OnItemClickListener {
    MeowBottomNavigation meowBottomNavigation;
    RecyclerView recyclerViewCourses;
    CourseAdapter courseAdapter;
    public  static  ArrayList<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        courseList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        meowBottomNavigation = findViewById(R.id.butonNavegation);
        setupBottomNavigation();
        setupCourses();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000); // repeated for every 1000 milleSeconds = 1 second ( infinite loop)

            }
        });

          setupRecyclerView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setupRecyclerView();
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
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    break;
            }
            return null;
        });
    }

    private void setupRecyclerView() {
//        ArrayList<Course> list = new ArrayList<>();
//        list.add(new Course("1211529","4:30","Adel","8/7","7:30"));
//        System.out.println("setupRecyclerView");

        courseAdapter = new CourseAdapter(courseList);
        courseAdapter.setOnItemClickListener(this);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCourses.setAdapter(courseAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_item_spacing);
        recyclerViewCourses.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    // عرض أيقونة سلة المحذوفات وتغيير الخلفية
                    CourseAdapter.CourseViewHolder holder = (CourseAdapter.CourseViewHolder) viewHolder;
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
                    CourseAdapter.CourseViewHolder holder = (CourseAdapter.CourseViewHolder) viewHolder;
                    holder.imageViewDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                CourseAdapter.CourseViewHolder holder = (CourseAdapter.CourseViewHolder) viewHolder;
                holder.imageViewDelete.setVisibility(View.GONE); // إخفاء أيقونة سلة المحذوفات
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewCourses);
    }

    public ArrayList<Course> setupCourses() {
        System.out.println("setupCourses");
        String id = LoginActivity.id;
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
                        HomeActivity.this.courseList.add(getCourseDetails(courseId));
                        System.out.println("Size list" + courseList.size());
                    } catch (JSONException exception) {
                        Log.d("Error", exception.toString());
                    }
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
        return courseList;
    }

    public Course getCourseDetails(String courseID) {
        System.out.println("getCourseDetails");
        final Course[] course = new Course[5];
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
                    course[0] = new Course(courseID, courseStartTime, courseDr, date, courseEndTime);
                    System.out.println("ID ----> " + course[0].getCourseID());
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
        return course[0];
    }

    private void showAlertForConfirmation(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this course ?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            courseAdapter.removeItem(position);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            courseAdapter.notifyItemChanged(position);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick(int position) {
        Course selectedCourse = courseList.get(position);
        Intent intent = new Intent(HomeActivity.this, TasksForCourse.class);
        intent.putExtra("courseID", selectedCourse.getCourseID());
        startActivity(intent);
    }

    public void action(View view) {
        setupRecyclerView();
    }
}