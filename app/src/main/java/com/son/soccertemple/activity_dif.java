package com.son.soccertemple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.son.soccertemple.quizDif.quiz_easy;
import com.son.soccertemple.quizDif.quiz_hard;
import com.son.soccertemple.quizDif.quiz_medium;

public class activity_dif extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Button btnBack, btnEasy, btnMedium, btnHard;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        btnEasy = (Button) findViewById(R.id.BtnEasy);
        btnMedium = (Button) findViewById(R.id.BtnMedium);
        btnHard = (Button) findViewById(R.id.BtnHard);
        btnBack = (Button) findViewById(R.id.BtnBack);

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent easy = new Intent(activity_dif.this, quiz_easy.class);
                startActivity(easy);
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent easy = new Intent(activity_dif.this, quiz_medium.class);
                startActivity(easy);
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent easy = new Intent(activity_dif.this, quiz_hard.class);
                startActivity(easy);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
