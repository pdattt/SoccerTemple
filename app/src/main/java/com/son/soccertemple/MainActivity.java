package com.son.soccertemple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import static com.son.soccertemple.R.raw.drump;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnHighscore, btnHistory, btnExit;
    TextView text1, text2;
    Handler handler = new Handler();
    Intent svc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mapping();
        setAnimationText();
        svc = new Intent(this, BackgroundService.class);
        startService(svc);

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
                stopService(svc);
                btnExit.startAnimation(fadeOutOnClick[0]);
                btnExit.startAnimation(fadeInOnClick[0]);
                finish();
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(svc);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(svc);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(svc);
    }
}