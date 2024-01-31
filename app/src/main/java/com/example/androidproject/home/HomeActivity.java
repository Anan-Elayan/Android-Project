package com.example.androidproject.home;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.androidproject.R;
import com.example.androidproject.Task.TasksForCourse;
import com.example.androidproject.addCourse.AddCourseActivity;
import com.example.androidproject.model.Course;
import com.example.androidproject.profile.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnItemClickListener{

    MeowBottomNavigation meowBottomNavigation;
    RecyclerView recyclerViewCourses;
    CourseAdapter courseAdapter;
    List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        meowBottomNavigation = findViewById(R.id.butonNavegation);

        setupBottomNavigation();
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
        courseList = new ArrayList<>(); // مكان لتحميل بيانات المساقات
        courseList.add(new Course("comp336", "2:00 PM - 3:00 PM", "Professor X"));

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
}
