package com.example.androidproject.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.androidproject.LoginAndRegister.LoginActivity;
import com.example.androidproject.LoginAndRegister.Register;
import com.example.androidproject.R;
import com.example.androidproject.addCourse.AddCourseActivity;
import com.example.androidproject.home.HomeActivity;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    Uri uriImg;
    private ImageView regesterImg;
    private ShapeableImageView userImg;
    MeowBottomNavigation meowBottomNavigation;
    TextView txtWelcomeMessage;
    Button btnEdit;
    Button btnUpdate;
    TextInputEditText textInputEditTextEmail;
    TextInputEditText textInputEditTextPassword;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;
    Intent intent;

    //344443
    String studentID = "1211529";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupViews();
        bottomNavigationSetUp();
        setupUserInfo(studentID);
        regesterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = uriImg.toString();
                String[]spletArrya = url.split("/");
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < spletArrya.length; i++) {
                    builder.append(spletArrya[i]);
                    builder.append("&");
                }
                System.out.println("builder.toString----> " + builder.toString());
                //builder.toString----> content:&&media&picker&0&com.android.providers.media.photopicker&media&1000000035&
                update(studentID,textInputEditTextEmail.getText().toString(),textInputEditTextPassword.getText().toString(), builder.toString());
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
                            String studentPassword = response.getString("studentPassword");
                            String urlImage = response.getString("studentImage");

                            String[]spletArrya = urlImage.split("&");
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < spletArrya.length; i++) {
                                builder.append(spletArrya[i]);
                                builder.append("/");

                            }
                            String str = builder.toString().substring(0,builder.toString().length()-1);
                            System.out.println("URL image--> " + urlImage.toString());
                            System.out.println("STR--> " + str);

//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(str));
//                            userImg.setImageBitmap(bitmap);


                            //Use Glide to load the image into ShapeableImageView
                            Glide.with(ProfileActivity.this)
                                    .load(userImg)
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

    private void update(String studentId, String studentEmail, String studentPassword,String image) {
        String url = "http://10.0.2.2:5000/updateStudent/" + studentId + "/" + studentEmail + "/" + studentPassword + "/" + image;

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
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInpuEditTextPassword);
        userImg = findViewById(R.id.userImg);
        regesterImg = findViewById(R.id.register_img);

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

    private void getImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "اختر صورة الحساب 🥰"), CHOOSE_IMAGE);
    }

}