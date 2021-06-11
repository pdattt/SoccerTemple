package com.son.soccertemple;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.Nullable;

public class activity_setup extends Activity {

    Button btnBack, btnNext;
    Spinner sp;
    Integer Number[] = {5, 6, 7, 8, 9, 10};
    EditText Name;
    TextView Error;
    ImageButton btnSound;
    int pos;
    Intent svc;

    //1 is on - 2 is off
    int music_mode = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Mapping();

        createSpin();
        svc = new Intent(this, BackgroundService.class);
        setSoundButton();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopSound();
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopSound();
                String userName = Name.getText().toString().trim();

                if(userName.equals("")) {
                    Error.setText("Vui lòng nhập vào tên người chơi!");
                    Error.setVisibility(View.VISIBLE);
                }
                else {
                    if(userName.length() >= 10) {
                        Error.setText("Tên không được dài quá 10 ký tự!");
                        Error.setVisibility(View.VISIBLE);
                        }
                    else {
                        int quizNum = 0;

                        switch (pos) {
                            case 0:
                                quizNum = 5;
                                break;
                            case 1:
                                quizNum = 6;
                                break;
                            case 2:
                                quizNum = 7;
                                break;
                            case 3:
                                quizNum = 8;
                                break;
                            case 4:
                                quizNum = 9;
                                break;
                            case 5:
                                quizNum = 10;
                                break;
                        }

                        Bundle bundle = new Bundle();

                        bundle.putString("userName", userName);
                        bundle.putInt("quizNum", quizNum);

                        Intent setDifficulty = new Intent(activity_setup.this, activity_dif.class);
                        setDifficulty.putExtra("setupPackage", bundle);
                        startActivity(setDifficulty);
                    }
                }
            }
        });
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

    private void setPopSound() {
        MediaPlayer pop = MediaPlayer.create(this,R.raw.pop);
        pop.start();
        pop.setLooping(false);
    }

    void Mapping() {
        Error = findViewById(R.id.TxtError);
        btnBack = findViewById(R.id.BtnBack);
        btnNext = findViewById(R.id.BtnNext);
        Name = findViewById(R.id.EdtName);
        sp =  findViewById(R.id.spinNumber);
        btnSound = findViewById(R.id.BtnSound);
    }

    void createSpin() {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>
                (this, android.R.layout.simple_spinner_dropdown_item, Number){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                tv.setTextColor(Color.parseColor("#629F6D"));

                // Return the view
                return view;
            }
        };

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

    @Override
    public void finish() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(svc);
    }
}
