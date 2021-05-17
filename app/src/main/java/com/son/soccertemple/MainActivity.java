package com.son.soccertemple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.son.soccertemple.R.raw.drump;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnHighscore, btnHistory, btnExit;
    TextView text1, text2;
    public MediaPlayer themeSong, ring;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mapping();
        setAnimationText();

        themeSong = MediaPlayer.create(this, drump);
        themeSong.setLooping(true);


        themeSong.start();

        final Animation[] fadeOutOnClick = new Animation[1];
        final Animation[] fadeInOnClick = new Animation[1];

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(MainActivity.this, activity_setup.class);
                fadeOutOnClick[0] = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout);
                fadeInOnClick[0] = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein);

                btnStart.startAnimation(fadeOutOnClick[0]);
                startActivity(game);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                btnStart.startAnimation(fadeInOnClick[0]);
            }
        });

        btnHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game = new Intent(MainActivity.this, activity_highscore.class);
                fadeOutOnClick[0] = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout);
                fadeInOnClick[0] = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein);

                btnHighscore.startAnimation(fadeOutOnClick[0]);
                startActivity(game);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                btnHighscore.startAnimation(fadeInOnClick[0]);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent history = new Intent(MainActivity.this, activity_history.class);
                fadeOutOnClick[0] = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout);
                fadeInOnClick[0] = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein);

                btnHistory.startAnimation(fadeOutOnClick[0]);
                startActivity(history);
                btnHistory.startAnimation(fadeInOnClick[0]);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadeOutOnClick[0] = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeout);
                fadeInOnClick[0] = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadein);
                themeSong.release();
                btnExit.startAnimation(fadeOutOnClick[0]);
                btnExit.startAnimation(fadeInOnClick[0]);
                finish();
            }
        });
    }

    public void onDestroy () {
        //do your stuff here
        themeSong.release();

        super.onDestroy();
    }

    private void setAnimationText() {
        final Animation[] slidetext = new Animation[2];
        slidetext[0] = AnimationUtils.loadAnimation(this,R.anim.lefttoright);
        slidetext[1] = AnimationUtils.loadAnimation(this,R.anim.lefttoright);

        slidetext[0].setDuration(600);
        slidetext[1].setDuration(700);

        text1.setVisibility(View.INVISIBLE);
        text2.setVisibility(View.INVISIBLE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text1.setVisibility(View.VISIBLE);
                text1.startAnimation(slidetext[0]);
            }
        }, 250);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text2.setVisibility(View.VISIBLE);
                text2.startAnimation(slidetext[1]);
            }
        }, 500);
    }

    private void Mapping() {
        btnStart = findViewById(R.id.BtnPlay);
        btnHistory = findViewById(R.id.BtnHistory);
        btnHighscore = findViewById(R.id.BtnHighScore);
        btnExit = findViewById(R.id.BtnExit);
        text1 = findViewById(R.id.Txt1);
        text2 = findViewById(R.id.Txt2);
    }
}