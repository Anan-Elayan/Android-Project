package com.example.androidproject.Task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.home.OnItemClickListener;
import com.example.androidproject.model.Task;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.taskViewHolder> {

    private List<Task> taskList;
    private static OnItemClickListener onItemClickListener;

    public TasksAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public taskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_details, parent, false);
        return new taskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull taskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textViewTaskTitle.setText(task.getTaskTitle());
        holder.textViewTaskTime.setText(task.getTaskTime());

    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void removeItem(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public static class taskViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTaskTitle, textViewTaskTime;
        public ImageView imageViewDelete;

        public taskViewHolder(View view) {
            super(view);
            textViewTaskTitle = view.findViewById(R.id.textViewTaskTitle);
            textViewTaskTime = view.findViewById(R.id.textViewTaskTime);
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

    public Task getCourseAtPosition(int position) {
        if (position >= 0 && position < taskList.size()) {
            return taskList.get(position);
        }
        return null;
    }
}

