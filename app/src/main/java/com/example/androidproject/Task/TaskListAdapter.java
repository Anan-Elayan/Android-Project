package com.example.androidproject.Task;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androidproject.R;
import com.example.androidproject.model.Task;

import java.util.ArrayList;

public class TaskListAdapter extends ArrayAdapter<Task> {

    public TaskListAdapter(@NonNull Context context, ArrayList<Task> dataArrayList)  {
        super(context, R.layout.list_row, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Task task = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }

        TextView txtTitle = view.findViewById(R.id.textViewTaskTitle);
        txtTitle.setText(task.getTaskTitle());
        TextView  txtTime = view.findViewById(R.id.textViewTaskTime);
        txtTime.setText (task.getTaskTime());
        return view;
    }
}
