package com.devcometvisionapp.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.devcometvisionapp.MainActivity;
import com.devcometvisionapp.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 1500;

    // Variables
    ImageView   cvappLogo;

    // Animations
    Animation scaleAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //Hooks
        cvappLogo = findViewById(R.id.cvapp_logo);
        scaleAnim = AnimationUtils.loadAnimation(this,R.anim.scale_anim);

        // set Animations on Elements
        cvappLogo.setAnimation(scaleAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMER);
    }
}