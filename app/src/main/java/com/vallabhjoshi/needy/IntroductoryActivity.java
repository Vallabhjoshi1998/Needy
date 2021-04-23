package com.vallabhjoshi.needy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class IntroductoryActivity extends AppCompatActivity
{
    ImageView img;
    LottieAnimationView lottieAnimationView;
    TextView myTextView, myLogoTextView;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_introductory);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        img = findViewById(R.id.img);
        lottieAnimationView = findViewById(R.id.lottie);
        myTextView = findViewById(R.id.needyTextView);
        myLogoTextView = findViewById(R.id.MyLogoTextView);



        lottieAnimationView.animate().translationX(1600).setDuration(1000).setStartDelay(2000);

        img.animate().translationY(550).scaleXBy(2.0f).scaleYBy(2.0f).setStartDelay(5000);
        myLogoTextView.animate().translationY(-145).setStartDelay(5000);
        myTextView.animate().translationY(-160).setStartDelay(5000);



        startButton = findViewById(R.id.StartButton);
        startButton.setVisibility(View.INVISIBLE);
        startButton.postDelayed(new Runnable() {
            public void run() {
                startButton.setVisibility(View.VISIBLE);
            }
        }, 7000);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroductoryActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });
    }
}