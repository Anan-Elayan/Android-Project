package com.example.androidproject.LoginAndRegister;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    Uri uriImg;
    private ImageView regesterImg;
    private ShapeableImageView userImg;
    private Button btnRegister;
    private TextInputEditText textInputEditTextStudentID;
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextView txtWarningID;
    private TextView txtWarningName;
    private TextView txtWarningEmail;
    private TextView txtWarningPassword;
    private TextView txtWarningImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupViews();
        regesterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
    }


    public void setupViews() {

        btnRegister = findViewById(R.id.button);
        textInputEditTextStudentID = findViewById(R.id.textInputEditTextStudetnID);
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        regesterImg = findViewById(R.id.register_img);
        userImg = findViewById(R.id.userImg);
        txtWarningID = findViewById(R.id.txtWarningID);
        txtWarningName = findViewById(R.id.txtWarningName);
        txtWarningEmail = findViewById(R.id.txtWarningEmail);
        txtWarningPassword = findViewById(R.id.txtWarningPassword);
        txtWarningImage = findViewById(R.id.txtWarningImage);

    }

    public void registerClicked(View view) {
        String studentID = textInputEditTextStudentID.getText().toString();
        String studentName = textInputEditTextName.getText().toString();
        String studentEmail = textInputEditTextEmail.getText().toString();
        String studentPassword = textInputEditTextPassword.getText().toString();
        String image = (uriImg != null) ? uriImg.toString() : "";

        if (studentID.isEmpty()) {
            txtWarningID.setVisibility(View.VISIBLE);
        }
        if (studentName.isEmpty()) {
            txtWarningName.setVisibility(View.VISIBLE);

        }
        if (studentEmail.isEmpty()) {
            txtWarningEmail.setVisibility(View.VISIBLE);
        }
        if (studentPassword.isEmpty()) {
            txtWarningPassword.setVisibility(View.VISIBLE);

        }
        if(image.isEmpty()){
            txtWarningImage.setVisibility(View.VISIBLE);
        }
        else {


            String url = "http://10.0.2.2:5000/getStudent";
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String id = obj.getString("studentID");
                            if (id.equals(studentID)) {
                                Toast.makeText(Register.this, "الك حساب من قبل يا وردة🥰",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException exception) {
                            Log.d("Error", exception.toString());
                        }
                    }
                    register(studentID, studentName, studentEmail, studentPassword, image);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Register.this, error.toString(),
                            Toast.LENGTH_SHORT).show();
                    Log.d("Error_json", error.toString());
                }
            });

            queue.add(request);
        }


    }

    private void register(String studentID, String studentName, String studentEmail, String studentPassword, String image) {
        String url = "http://10.0.2.2:5000/register";
        RequestQueue queue = Volley.newRequestQueue(Register.this);
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("studentID", studentID);
            jsonParams.put("studentName", studentName);
            jsonParams.put("studentEmail", studentEmail);
            jsonParams.put("studentPassword", studentPassword);
            jsonParams.put("studentImage", image);
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
                            System.out.println("try");
                            str = response.getString("result");
                            Intent intent = new Intent(Register.this, LoginActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            System.out.println("catch 2");
                            System.out.println(e.toString());
                            e.printStackTrace();
                        }

                        Toast.makeText(Register.this, str,
                                Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error");
                        Log.d("VolleyError", error.toString());
                    }
                }
        );
        queue.add(request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImg = data.getData();
            System.out.println("URI Image--> " + uriImg);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImg);
                userImg.setImageBitmap(bitmap);
                System.out.println("USER IMAGE --> " + userImg);
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