package com.vallabhjoshi.needy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
{
    Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.buttonLogOut);


    }

    public void OnLogOut(View view)
    {
        FirebaseAuth.getInstance().signOut(); //Logout
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}