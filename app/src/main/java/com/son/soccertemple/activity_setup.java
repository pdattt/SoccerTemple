package com.son.soccertemple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Collections;

public class activity_setup extends Activity {

    Button btnBack, btnNext;
    Spinner sp;
    Integer Number[] = {5, 6, 7, 8, 9, 10};
    EditText Name;
    int pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Mapping();
        createSpin();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = Name.getText().toString().trim();

                if(userName.equals("")) {
                    TextView Error = findViewById(R.id.TxtError);
                    Error.setVisibility(View.VISIBLE);
                }
                else {
                    int quizNum = 0;
                    switch(pos) {
                        case 0: quizNum = 5; break;
                        case 1: quizNum = 6; break;
                        case 2: quizNum = 7; break;
                        case 3: quizNum = 8; break;
                        case 4: quizNum = 9; break;
                        case 5: quizNum = 10; break;
                    }

                    Bundle bundle = new Bundle();

                    bundle.putString("userName", userName);
                    bundle.putInt("quizNum", quizNum);

                    Intent setDifficulty = new Intent(activity_setup.this, activity_dif.class);
                    setDifficulty.putExtra("setupPackage", bundle);
                    startActivity(setDifficulty);
                }
            }
        });
    }

    void Mapping() {
        btnBack = findViewById(R.id.BtnBack);
        btnNext = findViewById(R.id.BtnNext);
        Name = findViewById(R.id.EdtName);
        sp =  findViewById(R.id.spinNumber);
    }

    void createSpin() {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, Number);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pos = -1;
            }
        });
    }
}
