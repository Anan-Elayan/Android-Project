package com.example.androidproject.addCourse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.androidproject.LoginAndRegister.LoginActivity;
import com.example.androidproject.LoginAndRegister.Register;
import com.example.androidproject.home.HomeActivity;
import com.example.androidproject.model.Course;
import com.example.androidproject.profile.ProfileActivity;
import com.example.androidproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private SharedPreferences prefs;
    Button btnAdd;
    Intent intent;
    ArrayList<Course>courseIDToSpinner;
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setupViews();
        setupSpinnerValue();
        bottomNavigationSetUp();

        spinnerDr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadCourseDetails();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setupSharedPrefs();
        ColorMode();

    }

    public void setupViews() {
        btnAdd = findViewById(R.id.btnAdd);
        spinnerCourse = findViewById(R.id.spinnerCourse);
        spinnerDr = findViewById(R.id.spinnerDr);
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEndTime = findViewById(R.id.txtEndTime);
        txtDate = findViewById(R.id.txtDate);
        constraintLayout = findViewById(R.id.constraintLayout);


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
        String courseid = spinnerCourse.getSelectedItem().toString();
        String instructor = spinnerDr.getSelectedItem().toString();
        String startTime = txtStartTime.getText().toString();
        String endTime = txtEndTime.getText().toString();
        String date = txtDate.getText().toString();
        if(courseid.isEmpty()){
                txtWarningCourseID.setVisibility(View.VISIBLE);
        }
        if(instructor.isEmpty()){
            txtWarningDr.setVisibility(View.VISIBLE);
        }

        else {
            addToBackEnd(LoginActivity.id, courseid, instructor);
        }
    }

    public void addToBackEnd(String studentID, String courseID, String courseDr) {
        String url = "http://10.0.2.2:5000/registerationCourse/" + studentID + "/" + courseID + "/" + courseDr ;
        RequestQueue queue = Volley.newRequestQueue(AddCourseActivity.this);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("studentID", studentID);
            jsonParams.put("courseID", courseID);
            jsonParams.put("courseDr", courseDr);
            
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
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                courseIDToSpinner =  new ArrayList<>();
                ArrayList<String>showDataList =  new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String courseID = obj.getString("courseID");
                        String courseDr = obj.getString("courseDr");
                        String courseEndTime = obj.getString("courseEndTime");
                        String courseStartTime = obj.getString("courseStartTime");
                        String date = obj.getString("date");
                        Course course = new Course(courseID,courseStartTime,courseDr,date,courseEndTime);
                        courseIDToSpinner.add(course);
                        if(!showDataList.contains(courseID)){
                            showDataList.add(courseID);
                        }
                    } catch (JSONException exception) {
                        Log.d("Error", exception.toString());
                    }
                }

                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddCourseActivity.this,
                        android.R.layout.simple_spinner_item, showDataList);

                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Apply the adapter to the spinner
                spinnerCourse.setAdapter(adapter);

                spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        fillSpinnerDr();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddCourseActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.d("Error_json", error.toString());
            }
        });
        queue.add(request);
    }

    public  void fillSpinnerDr(){
        ArrayList<String>doctorList= new ArrayList<>();
        for (int i = 0; i < courseIDToSpinner.size(); i++) {
            if(courseIDToSpinner.get(i).getCourseID().equals(spinnerCourse.getSelectedItem().toString())){
                doctorList.add(courseIDToSpinner.get(i).getProfessorName());
            }
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddCourseActivity.this,
                android.R.layout.simple_spinner_item, doctorList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerDr.setAdapter(adapter);
    }


    public  void loadCourseDetails(){
        String selectedCourse = spinnerCourse.getSelectedItem().toString();
        String selectedDr = spinnerDr.getSelectedItem().toString();
        for (int i = 0; i < courseIDToSpinner.size(); i++) {
            if(selectedCourse.equals(courseIDToSpinner.get(i).getCourseID()) && selectedDr.equals(courseIDToSpinner.get(i).getProfessorName())){
                txtStartTime.setText(courseIDToSpinner.get(i).getStartTime());
                txtEndTime.setText(courseIDToSpinner.get(i).getEndTime());
                txtDate.setText(courseIDToSpinner.get(i).getDate());
            }
        }
    }


    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }


    private void ColorMode(){
        boolean dark_mode = prefs.getBoolean("DARK MODE",false);
        if(dark_mode){
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.blackModeColor));
            txtStartTime.setTextColor(getResources().getColor(R.color.white));
            txtEndTime.setTextColor(getResources().getColor(R.color.white));
            txtDate.setTextColor(getResources().getColor(R.color.white));
            txtEndTime.setHintTextColor(getResources().getColor(R.color.white));
            txtStartTime.setHintTextColor(getResources().getColor(R.color.white));
            txtDate.setHintTextColor(getResources().getColor(R.color.white));

        }
    }

}

