package com.example.androidproject.LoginAndRegister;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.example.androidproject.home.HomeActivity;
import com.google.android.material.imageview.ShapeableImageView;

public class LoginActivity extends AppCompatActivity {


    private ImageView regesterImg;
    private static final int CHOOSE_IMAGE = 101;
    Uri uriImg;
    Button button;


    private ShapeableImageView userImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button = findViewById(R.id.button);

        button = findViewById(R.id.button);


//        regesterImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "اختر صورة الحساب 🥰"), CHOOSE_IMAGE);
//            }
//        });
//
//
//
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
//            uriImg = data.getData();
//            try{
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImg);
//                userImg.setImageBitmap(bitmap);
//            }catch (Exception e){
//            }
//        }
    }

    public void ActionLogin(View view) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void ActionRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, Register.class);
        startActivity(intent);

    }
}