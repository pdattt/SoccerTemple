package com.son.soccertemple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class activity_setup extends Activity {

    Button btnBack, btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        btnBack = (Button)findViewById(R.id.BtnBack);
        btnNext = (Button)findViewById(R.id.BtnNext);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setDifficulty = new Intent(activity_setup.this, activity_dif.class);
                startActivity(setDifficulty);
            }
        });

    }
}
