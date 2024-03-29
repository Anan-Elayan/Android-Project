package com.example.androidproject.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.androidproject.LoginAndRegister.LoginActivity;
import com.example.androidproject.R;
import com.example.androidproject.addCourse.AddCourseActivity;
import com.example.androidproject.home.HomeActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    private Switch switchMode;
    private ConstraintLayout constraintLayout;
    Uri uriImg;
    private ShapeableImageView userImg;
    MeowBottomNavigation meowBottomNavigation;
    TextView txtWelcomeMessage;
    TextView txtEmailProfileEmail;
    TextView txtWarningProfilePassword;
    Button btnUpdate;
    Button btnLogOut;
    TextInputEditText textInputEditTextEmail;
    TextInputEditText textInputEditTextPassword;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;
    Intent intent;
    String id = LoginActivity.id;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupViews();
        bottomNavigationSetUp();
        setupUserInfo(id);
        setupSharedPrefs();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textInputEditTextEmail.getText().toString();
                String password = textInputEditTextPassword.getText().toString();
                if(email.isEmpty()){
                    txtEmailProfileEmail.setVisibility(View.VISIBLE);
                }if(password.isEmpty()){
                    txtWarningProfilePassword.setVisibility(View.VISIBLE);
                }else {
                    update(id, email, password);
                }
            }
        });
        setupTheam();
        setupMode();



    }

    public  void setupTheam(){
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Change background color based on switch state
                if (isChecked) {

                    constraintLayout.setBackgroundColor(getResources().getColor(R.color.blackModeColor));
                    //  HomeActivity.constraintHome.setBackgroundColor(getResources().getColor(R.color.black));
                    txtWelcomeMessage.setTextColor(getResources().getColor(R.color.white));
                    textInputEditTextEmail.setTextColor(getResources().getColor(R.color.white));
                    textInputEditTextPassword.setTextColor(getResources().getColor(R.color.white));

                    editor.putBoolean("DARK MODE", true);
                    editor.commit();

                } else {
                    constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    // HomeActivity.constraintHome.setBackgroundColor(getResources().getColor(R.color.white));
                    txtWelcomeMessage.setTextColor(getResources().getColor(R.color.blackModeColor));
                    textInputEditTextEmail.setTextColor(getResources().getColor(R.color.blackModeColor));
                    textInputEditTextPassword.setTextColor(getResources().getColor(R.color.blackModeColor));
                    editor.putBoolean("DARK MODE", false);
                    editor.commit();
                }
            }
        });

    }

    public void setupMode(){
        boolean flag = prefs.getBoolean("DARK MODE", false);

        switchMode.setChecked(flag);
    }



    private void setupUserInfo(String studentId) {
        String url = "http://10.0.2.2:5000/getStudent/" + studentId;
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
                            String studentPassword = response.getString("studentPassword");
                            String urlImage = response.getString("studentImage");


                            //Use Glide to load the image into ShapeableImageView
                            Glide.with(ProfileActivity.this)
                                    .load(Uri.parse(urlImage))
                                    .into(userImg);

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


    public void setupViews() {
        txtWelcomeMessage = findViewById(R.id.txtWelcomMessage);
        btnUpdate = findViewById(R.id.btnUpdate);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInpuEditTextPassword);
        userImg = findViewById(R.id.userImg);
        btnLogOut = findViewById(R.id.btnLogOut);
        switchMode = findViewById(R.id.switchMode);
        constraintLayout = findViewById(R.id.constraintLayout);
        txtEmailProfileEmail = findViewById(R.id.txtEmailProfileEmail);
        txtWarningProfilePassword = findViewById(R.id.txtWarningProfilePassword);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImg = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImg);
                userImg.setImageBitmap(bitmap);
            } catch (Exception e) {
            }
        }
    }


    public void actionLogOut(View view) {
        System.out.println("action");
        Intent intent1 = new Intent(ProfileActivity.this, LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
        finish();
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }
}