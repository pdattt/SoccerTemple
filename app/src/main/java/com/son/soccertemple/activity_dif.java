package com.son.soccertemple;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.son.soccertemple.quizDif.quiz_easy;
import com.son.soccertemple.quizDif.quiz_hard;
import com.son.soccertemple.quizDif.quiz_medium;

public class activity_dif extends Activity {

    Button btnBack, btnEasy, btnMedium, btnHard;
    ImageButton btnSound;
    int music_mode = 1;
    Intent svc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        svc = new Intent(this, BackgroundService.class);

        Mapping();
        setSoundButton();

        selectMode(btnEasy, quiz_easy.class);
        selectMode(btnMedium, quiz_medium.class);
        selectMode(btnHard, quiz_hard.class);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopSound();
                finish();
            }
        });
    }

    private void selectMode(Button btn, Class className) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopSound();
                Intent callerIntent = getIntent();
                final Bundle packageFromCaller = callerIntent.getBundleExtra("setupPackage");
                Intent hard = new Intent(activity_dif.this, className);
                hard.putExtra("setupPackage", packageFromCaller);
                startActivity(hard);
            }
        });
    }

    private void setPopSound() {
        MediaPlayer pop = MediaPlayer.create(this,R.raw.pop);
        pop.start();
        pop.setLooping(false);
    }

    private void setSoundButton() {

        btnSound.setBackground(getResources().getDrawable(R.drawable.icon_unmute));

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopSound();
                if(music_mode == 1) {
                    stopService(svc);
                    btnSound.setBackground(getResources().getDrawable(R.drawable.icon_mute));
                    music_mode = 2;
                }
                else {
                    startService(svc);
                    btnSound.setBackground(getResources().getDrawable(R.drawable.icon_unmute));
                    music_mode = 1;
                }
            }
        });
    }

    private void Mapping() {
        btnEasy = findViewById(R.id.BtnEasy);
        btnMedium = findViewById(R.id.BtnMedium);
        btnHard = findViewById(R.id.BtnHard);
        btnBack = findViewById(R.id.BtnBack);
        btnSound = findViewById(R.id.BtnSound);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(svc);
    }
}
