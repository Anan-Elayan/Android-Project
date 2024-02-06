package com.example.androidproject.Task;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.R;
import com.example.androidproject.home.HomeActivity;
import com.example.androidproject.model.Task;
import java.util.ArrayList;

import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class ListAdapter extends ArrayAdapter<Task> {
    private Context mContext;
    private int mResource;

    private ImageButton imageButton;
    private ArrayList<Task>listData;

    public ListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Task> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.listData = objects;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);
        Task item = getItem(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(mResource, parent, false);
        }
        TextView txtTitle = convertView.findViewById(R.id.textViewTaskTitle);
        txtTitle.setText(item.getTaskTitle());
        TextView  txtTime = convertView.findViewById(R.id.textViewTaskTime);
        txtTime.setText (item.getTaskTime());

//        imageButton  = convertView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListAdapter.this.getContext());
                builder.setMessage("Are you sure you want to delete this task ?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    String url = "http://10.0.2.2:5000/deleteTask/" + item.getTaskID();
                    RequestQueue queue = Volley.newRequestQueue(ListAdapter.this.getContext());

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
                                        Toast.makeText(ListAdapter.this.getContext(), message, Toast.LENGTH_SHORT).show();
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
                    listData.remove(position);
                    notifyDataSetChanged();
                });
                builder.setNegativeButton("No", (dialog, which) -> {

                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return convertView;
    }

    public Task getItemByPosition(int position) {
        return getItem(position);
    }




}
