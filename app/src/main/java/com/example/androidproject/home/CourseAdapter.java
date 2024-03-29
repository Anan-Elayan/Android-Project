package com.example.androidproject.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.model.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private static OnItemClickListener onItemClickListener;

    public CourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.textViewCourseName.setText(course.getCourseID());
        holder.textViewCourseTime.setText(course.getStartTime());
        holder.textViewProfessorName.setText(course.getProfessorName());
    }


    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public void removeItem(int position) {
        courseList.remove(position);
        notifyItemRemoved(position);
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCourseName, textViewCourseTime, textViewProfessorName;
        public ImageView imageViewDelete;

        public CourseViewHolder(View view) {
            super(view);
            textViewCourseName = view.findViewById(R.id.textViewTaskTitle);
            textViewCourseTime = view.findViewById(R.id.textViewTaskTime);
            textViewProfessorName = view.findViewById(R.id.textViewProfessorName);
            imageViewDelete = view.findViewById(R.id.imageViewDelete);

            itemView.setOnClickListener(e -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    public Course getCourseAtPosition(int position) {
        if (position >= 0 && position < courseList.size()) {
            return courseList.get(position);
        }
        return null;
    }
}

