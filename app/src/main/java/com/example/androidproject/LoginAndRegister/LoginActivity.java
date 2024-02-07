package com.example.androidproject.LoginAndRegister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.R;
import com.example.androidproject.home.HomeActivity;
import com.example.androidproject.model.Student;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    ArrayList<Student> studentList = new ArrayList<>();
    private RequestQueue queue;
    private EditText studentID;
    private EditText pass;
    private CheckBox remCh;
    public static String id;
    private ConstraintLayout constraintLayout;
    private TextView welcome;
    private TextView txtWarningLoginStudentID;
    private TextView txtWarningLoginPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupViews();
        LoadData();
        setupSharedPrefs();
        ColorMode();
        checkRememberMe();
    }

    public void setupViews() {
        studentID = findViewById(R.id.studentID);
        pass = findViewById(R.id.pass);
        constraintLayout = findViewById(R.id.constraintLayout);
        welcome = findViewById(R.id.welcome);
        remCh = findViewById(R.id.rememberMe);
        txtWarningLoginStudentID = findViewById(R.id.txtWarningLoginStudentID);
        txtWarningLoginPassword = findViewById(R.id.txtWarningLoginPassword);
        queue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void ActionLogin(View view) {
        if(studentID.getText().toString().isEmpty()){
            txtWarningLoginStudentID.setVisibility(View.VISIBLE);
            return;
        }if(pass.getText().toString().isEmpty()){
            txtWarningLoginPassword.setVisibility(View.VISIBLE);
            return;
        }
        id = studentID.getText().toString();
        int isExist = Check(); // to check the input data if exist or no
        if (isExist == 1) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);

            Toast.makeText(LoginActivity.this, "يا هلا و مرحبا، نورت 😁",
                    Toast.LENGTH_LONG).show();
            txtWarningLoginStudentID.setVisibility(View.INVISIBLE);
            txtWarningLoginPassword.setVisibility(View.INVISIBLE);
        }

        else {
            Toast.makeText(LoginActivity.this, "  شو رأيك تسجل حساب عشان فش عندك حساب 😒",
                    Toast.LENGTH_LONG).show();
            txtWarningLoginStudentID.setVisibility(View.INVISIBLE);
            txtWarningLoginPassword.setVisibility(View.INVISIBLE);
        }

        if (remCh.isChecked()) {
            editor.putString("ID", studentID.getText().toString());
            editor.putString("PASS", pass.getText().toString());
            editor.putBoolean("FLAG", true);
            editor.commit();
        }
    }

    public void ActionRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, Register.class);
        startActivity(intent);

    }

    private void LoadData() {
        String url = "http://10.0.2.2:5000//getStudent";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String studentID = jsonObject.getString("studentID");
                                String name = jsonObject.getString("studentName");
                                String email = jsonObject.getString("studentEmail");
                                String pass = jsonObject.getString("studentPassword");
                                Student student = new Student(name, studentID, email, pass);
                                studentList.add(student);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
        queue.add(jsonArrayRequest);
    }


    private int Check() {
        String EditTxtID = studentID.getText().toString();
        String EditTxtPass = pass.getText().toString();
        for (Student student : studentList) {
            if (student.getStudentID().equals(EditTxtID)) {
                if (student.getPass().equals(EditTxtPass)) {
                    return 1;
                }
                return 0;
            }
        }
        return -1;
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void checkRememberMe() {
        boolean flag = prefs.getBoolean("FLAG", false);
        if (flag) {
            String name = prefs.getString("ID", "");
            String password = prefs.getString("PASS", "");
            studentID.setText(name);
            pass.setText(password);
            remCh.setChecked(true);
        }
    }

    private void ColorMode() {
        boolean dark_mode = prefs.getBoolean("DARK MODE", false);
        if (dark_mode) {
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.blackModeColor));
            welcome.setTextColor(getResources().getColor(R.color.white));
            remCh.setTextColor(getResources().getColor(R.color.white));
            studentID.setTextColor(getResources().getColor(R.color.white));
            studentID.setHintTextColor(getResources().getColor(R.color.white));
            pass.setTextColor(getResources().getColor(R.color.white));
            pass.setHintTextColor(getResources().getColor(R.color.white));
        }
    }
}