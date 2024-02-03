package com.example.androidproject.addCourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.androidproject.LoginAndRegister.LoginActivity;
import com.example.androidproject.LoginAndRegister.Register;
import com.example.androidproject.home.HomeActivity;
import com.example.androidproject.profile.ProfileActivity;
import com.example.androidproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AddCourseActivity extends AppCompatActivity {

    MeowBottomNavigation meowBottomNavigation;

    Spinner spinnerCourse;
    Spinner spinnerDr;
    TextInputEditText txtStartTime;
    TextInputEditText txtEndTime;
    TextInputEditText txtDate;


    TextView txtWarningCourseID;
    TextView txtWarningDr;
    TextView txtWarningStartTime;
    TextView txtWarningEndTime;
    TextView txtWarningdate;
    Button btnAdd;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setupViews();
        setupSpinnerValue();
        bottomNavigationSetUp();

    }

    public void setupViews() {
        btnAdd = findViewById(R.id.btnAdd);
        spinnerCourse = findViewById(R.id.spinnerCourse);
        spinnerDr = findViewById(R.id.spinnerDr);
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEndTime = findViewById(R.id.txtEndTime);
        txtDate = findViewById(R.id.txtDate);
        txtWarningCourseID = findViewById(R.id.txtWarningCourseID);
        txtWarningdate = findViewById(R.id.txtWarningdate);


    }

    private void bottomNavigationSetUp() {
        meowBottomNavigation = findViewById(R.id.butonNavegation);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.add_circle));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.icon_material_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.account_circle));
        meowBottomNavigation.show(1, true);


        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        intent = new Intent(AddCourseActivity.this, AddCourseActivity.class);
                        startActivity(intent);
                        break;

                    case 2:
                        intent = new Intent(AddCourseActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;

                    case 3:
                        intent = new Intent(AddCourseActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return null;
            }
        });

        meowBottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                return null;
            }
        });
    }


    public void actionAdd(View view) {
        String id = spinnerCourse.getSelectedItem().toString();
        String instructor = spinnerDr.getSelectedItem().toString();
        String startTime = txtStartTime.getText().toString();
        String endTime = txtEndTime.getText().toString();
        String date = txtDate.getText().toString();
        if(id.isEmpty()){
                txtWarningCourseID.setVisibility(View.VISIBLE);
        }
        if(instructor.isEmpty()){
            txtWarningDr.setVisibility(View.VISIBLE);
        }
        if(startTime.isEmpty()){
            txtWarningStartTime.setVisibility(View.VISIBLE);
        }
        if(endTime.isEmpty()){
            txtWarningEndTime.setVisibility(View.VISIBLE);
        }
        if(date.isEmpty()){
            txtWarningdate.setVisibility(View.VISIBLE);
        }
        else {
            addToBackEnd(id, instructor, startTime, endTime, date);
        }
    }

    public void addToBackEnd(String courseID, String doctor, String startTime, String endTime, String date) {
        String url = "http://10.0.2.2:5000/addNewCourse/" + courseID + "/" + doctor + "/" + startTime + "/" + endTime + "/" + date;
        RequestQueue queue = Volley.newRequestQueue(AddCourseActivity.this);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("courseID", courseID);
            jsonParams.put("courseDr", doctor);
            jsonParams.put("courseStartTime", startTime);
            jsonParams.put("courseEndTime", endTime);
            jsonParams.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JsonObjectRequest with POST method
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String str = "";
                        try {
                            str = response.getString("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(AddCourseActivity.this, str,
                                Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyError", error.toString());
                    }
                }
        );
        queue.add(request);
    }


    public  void setupSpinnerValue(){
        String url = "http://10.0.2.2:5000/getAllCourses" ;
        RequestQueue queue = Volley.newRequestQueue(AddCourseActivity.this);
        JSONObject jsonParams = new JSONObject();
//        try {
//            jsonParams.put("courseID", courseID);
//            jsonParams.put("courseDr", doctor);
//            jsonParams.put("courseStartTime", startTime);
//            jsonParams.put("courseEndTime", endTime);
//            jsonParams.put("date", date);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        // Create a JsonObjectRequest with POST method
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String str = "";
                        try {
                            str = response.getString("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(AddCourseActivity.this, str,
                                Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VolleyError", error.toString());
                    }
                }
        );
        queue.add(request);

    }
}

