package com.example.androidproject.LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaCodec;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    private Button btnRegister;
    private TextInputEditText textInputEditTextStudentID;
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupViews();
    }


    public void setupViews(){

        btnRegister = findViewById(R.id.btnRegister);
        textInputEditTextStudentID = findViewById(R.id.textInputEditTextStudetnID);
        textInputEditTextName = findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);

    }
    public void registerClicked(View view) {
        String studentID = textInputEditTextStudentID.getText().toString();
        String studentName = textInputEditTextName.getText().toString();
        String studentEmail = textInputEditTextEmail.getText().toString();
        String studentPassword = textInputEditTextPassword.getText().toString();

        register(studentID, studentName, studentEmail,studentPassword);
    }

    private void register(String studentID, String studentName, String studentEmail , String studentPassword){
        System.out.println("pppppppppppppppppppppppppppppppppppppppp");
        String url = "http://10.0.2.2:85/register";

        RequestQueue queue = Volley.newRequestQueue(Register.this);



        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("studentID", studentID);
            jsonParams.put("studentName", studentName);
            jsonParams.put("studentEmail", studentEmail);
            jsonParams.put("studentPassword", studentPassword);
            System.out.println("try 1");

        } catch (JSONException e) {
            System.out.println("catch 1");
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
        // below line is to make
        // a json object request.
        queue.add(request);
    }

}