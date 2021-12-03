package com.example.istudyspace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class Intro extends AppCompatActivity {

    ImageView logo, app_name, bg;
    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        logo = findViewById(R.id.logo);
        app_name = findViewById(R.id.app_name);
        bg = findViewById(R.id.bg);

        lottie = findViewById(R.id.splash);

        bg.animate().translationY(-3400).setDuration(1000).setStartDelay(4100);
        logo.animate().translationY(2800).setDuration(1000).setStartDelay(4100);
        app_name.animate().translationY(2600).setDuration(1000).setStartDelay(4100);
        lottie.animate().translationY(2600).setDuration(1000).setStartDelay(4100);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }, 5100);
    }
}