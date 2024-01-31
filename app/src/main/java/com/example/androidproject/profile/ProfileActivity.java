package com.example.androidproject.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.androidproject.LoginAndRegister.LoginActivity;
import com.example.androidproject.LoginAndRegister.Register;
import com.example.androidproject.R;
import com.example.androidproject.addCourse.AddCourseActivity;
import com.example.androidproject.home.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileActivity extends AppCompatActivity {

    MeowBottomNavigation meowBottomNavigation;
    TextView txtWelcomeMessage;
    Button btnEdit;
    Button btnUpdate;
    TextInputEditText textInputEditTextEmail;
    TextInputEditText textInputEditTextPassword;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;
    Intent intent;

    String studentID = "1210341";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupViews();
        bottomNavigationSetUp();
        setupUserInfo(studentID);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                update(studentID,textInputEditTextEmail.getText().toString(),textInputEditTextPassword.getText().toString());
            }
        });
    }

    private void setupUserInfo(String studentId){
        String url = "http://10.0.2.2:5000/getStudent/"+studentId;

        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);

        // Create a JsonObjectRequest with GET method
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,  // No request parameters for a GET request
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract values from the JSON response
                            String studentEmail = response.getString("studentEmail");
                            String studentName = response.getString("studentName");
                            String studentImage = response.getString("studentImage");
                            String studentPassword = response.getString("studentPassword");

                            txtWelcomeMessage.setText("Hello " + studentName);
                            textInputEditTextEmail.setText(studentEmail);
                            textInputEditTextPassword.setText(studentPassword);

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
    }

    private void update(String studentId, String studentEmail, String studentPassword) {
        String url = "http://10.0.2.2:5000/updateStudent/" + studentId + "/" + studentEmail + "/" + studentPassword;

        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);

        // Create a JsonObjectRequest with PUT method
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Handle the response as needed
                            String message = response.getString("message");
                            Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
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
    }


    public void setupViews(){
        txtWelcomeMessage = findViewById(R.id.txtWelcomMessage);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnEdit = findViewById(R.id.btnEdit);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInpuEditTextPassword);
    }

    private void bottomNavigationSetUp() {
        meowBottomNavigation = findViewById(R.id.butonNavegation);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.add_circle));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.icon_material_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.account_circle));
        meowBottomNavigation.show(3, true);


        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        intent = new Intent(ProfileActivity.this, AddCourseActivity.class);
                        startActivity(intent);
                        break;

                    case 2:
                        intent = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;

                    case 3:
                        intent = new Intent(ProfileActivity.this, ProfileActivity.class);
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
}