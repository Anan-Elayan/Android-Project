package com.example.androidproject.splash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.androidproject.R;


public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    //comment

    int images[] = {R.drawable.first_image, R.drawable.second_image, R.drawable.third_image,};

    int title[] = {R.string.title_one, R.string.title_two, R.string.title_three,};
    int hidene[] = {R.string.hiden_one,R.string.hiden_two,R.string.hiden_three};




    public ViewPagerAdapter(Context context){
        this.context = context;
    }


    @Override
    public int getCount() {
        return  title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_splash_splash,container,false);

        ImageView slideImage = (ImageView) view.findViewById(R.id.imageView2);
        TextView slideTitle = (TextView) view.findViewById(R.id.textViewTitle);
        TextView slideHidden = (TextView) view.findViewById(R.id.textViewHideen);

        Animation imageInimation = AnimationUtils.loadAnimation(context,R.anim.zoom_animation);
        slideImage.startAnimation(imageInimation);


        // Apply animation to all ImageView instances in the layout
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView2);
            slideImage.startAnimation(imageInimation);

            // Delay the text animation by 1000 milliseconds (1 second)
            int delayMillis = 1000;
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    slideTitle.setVisibility(View.VISIBLE);
                    slideHidden.setVisibility(View.VISIBLE);
                    // Apply text animations after the delay
                    Animation textAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_animation);
                    slideTitle.startAnimation(textAnimation);
                    slideHidden.startAnimation(textAnimation);
                }
            }, delayMillis);

        }

        slideImage.setImageResource(images[position]);
        slideTitle.setText(title[position]);
        slideHidden.setText(hidene[position]);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
