package com.example.androidproject.splash;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.androidproject.LoginAndRegister.LoginActivity;
import com.example.androidproject.R;


public class MainActivity extends AppCompatActivity {

    Button btnNext,btnSkip,btnBack;
    ViewPager viewPager;
    LinearLayout dotLayout;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View decordView = getWindow().getDecorView();
        decordView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_main);

        btnBack = findViewById(R.id.btnBack);
        btnSkip = findViewById(R.id.btnSkip);
        btnNext  = findViewById(R.id.btnNext);
        viewPager =  findViewById(R.id.viewPager);
        dotLayout = (LinearLayout) findViewById(R.id.buttomLinerLayout);


        viewPagerAdapter = new ViewPagerAdapter(this); // Updated to pass FragmentManager
        viewPager.setAdapter(viewPagerAdapter);
        setupIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);

    }



    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setupIndicator(position);
            if (position > 0){
                btnBack.setVisibility(View.VISIBLE);
            }else {
                btnBack.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    public void setupIndicator(int position){

        dots = new TextView[3];
        dotLayout.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(33);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            dotLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));

    }


    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
    }

    public void actionButtonSkip(View view) {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void actionButtonNext(View view) {
        int currentItem = getItem(0);
        if (currentItem<2)
            viewPager.setCurrentItem(getItem(1),true);
        else {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void actionButtonBack(View view) {
        if (getItem(0) > 0){
            viewPager.setCurrentItem(getItem(-1),true);
        }
    }



}