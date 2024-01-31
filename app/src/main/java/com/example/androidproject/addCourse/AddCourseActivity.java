package com.example.androidproject.addCourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.androidproject.home.HomeActivity;
import com.example.androidproject.profile.ProfileActivity;
import com.example.androidproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AddCourseActivity extends AppCompatActivity {

    MeowBottomNavigation meowBottomNavigation;
    TextInputLayout textInputLayoutCourseID;
    TextInputLayout textInputLayoutInstructor;
    TextInputLayout textInputLayoutStarTime;
    TextInputLayout textInputLayoutEndTime;
    TextInputEditText textInputEditTextCourseID;
    TextInputEditText textInputEditTextInstructor;
    TextInputEditText textInputEditTextStarTime;
    TextInputEditText textInputEditTextEndTime;
    RadioButton saturday,monday ,wednesday,tuesday,thursday;
    RadioGroup group;
    Button btnAdd;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setupViews();
        bottomNavigationSetUp();

    }

    public  void setupViews(){
        btnAdd = findViewById(R.id.btnAdd);
        saturday = findViewById(R.id.radioButtonS);
        monday = findViewById(R.id.radioButtonM);
        wednesday = findViewById(R.id.radioButtonW);
        tuesday = findViewById(R.id.radioButtonT);
        thursday = findViewById(R.id.radioButtonR);
        textInputEditTextCourseID = findViewById(R.id.textInputEditTextCourseId);
        textInputEditTextInstructor = findViewById(R.id.textInputEditTextInstructor);
        textInputEditTextStarTime = findViewById(R.id.textInputEditTextStartTime);
        textInputEditTextEndTime = findViewById(R.id.textInputEditTextEndTime);


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



}

