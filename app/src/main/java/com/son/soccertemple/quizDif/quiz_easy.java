package com.son.soccertemple.quizDif;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.son.soccertemple.BackgroundService;
import com.son.soccertemple.Player;
import com.son.soccertemple.R;
import com.son.soccertemple.activity_result;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class quiz_easy extends Activity {
    private ArrayList<Player> list = new ArrayList<>();
    private ArrayList<Player> quizList = new ArrayList<>();

    ImageView Image;
    Button AnswerA, AnswerB, AnswerC, AnswerD;
    Player correctAnswer;
    LinearLayout layout;
    TextView quizNo, hint, countdown;
    CountDownTimer countDownTimer;
    Intent svc;
    ImageButton btnSound;

    int pos = 0;
    int res = 0;
    int score = 0;
    int point = 50;
    int number;
    int hint_remain = 3;
    int timeCountdown = 15;
    boolean hint_mode = true;
    int music_mode = 1;
    String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_easy);

        svc = new Intent(this, BackgroundService.class);
        Mapping();
        //Receive bundle package
        //Create a list with a parameter is the number of the quiz
        Intent callerIntent = getIntent();
        Bundle packageFromCaller = callerIntent.getBundleExtra("setupPackage");

        number = packageFromCaller.getInt("quizNum");
        userName = packageFromCaller.getString("userName");
        CreatePlayerList(number);
        setSoundButton();

        Display(pos);
        startCountdownTimer();

        setAnswerOnClick(AnswerA, number);
        setAnswerOnClick(AnswerB, number);
        setAnswerOnClick(AnswerC, number);
        setAnswerOnClick(AnswerD, number);
    }

    private void startCountdownTimer() {
        Handler handler = new Handler();

        countDownTimer = new CountDownTimer(timeCountdown * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(String.valueOf(millisUntilFinished/1000));

                if(millisUntilFinished/1000 <= 1) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            countdown.setText(String.valueOf(0));
                        }
                    },800);
                }
            }

            @Override
            public void onFinish() {
                pos++;
                countDownTimer.cancel();
                if(pos >= number)
                    FinishQuiz();
                else {
                    Display(pos);
                    startCountdownTimer();
                }
            }
        };

        countdown.setText(String.valueOf(timeCountdown));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                countDownTimer.start();
            }
        },1000);
    }

    private void setAnswerOnClick(final Button BT, final int number) {
        BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopSound();
                if(BT.getText().toString().equals(correctAnswer.getName())) {
                    res++;
                    score += point;
                }

                pos++;
                countDownTimer.cancel();
                if(pos >= number)
                    FinishQuiz();
                else {
                    Display(pos);
                    startCountdownTimer();
                }
            }
        });
    }

    private void setPopSound() {
        MediaPlayer pop = MediaPlayer.create(this,R.raw.pop);
        pop.start();
        pop.setLooping(false);
    }

    private void FinishQuiz() {
        final Intent intent = new Intent(quiz_easy.this, activity_result.class);

        //pass bundle package
        Bundle bundle = new Bundle();
        bundle.putString("Name", userName);
        bundle.putInt("Score", score);
        bundle.putInt("NoOfQuiz", number);
        bundle.putInt("NoOfCorrect", res);
        intent.putExtra("resultPackage", bundle);
        stopService(svc);
        playWhitsle();
        //start result Activity
        //delay for 1,5s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout.removeAllViews();
                startActivity(intent);
                finish();
            }
        },3000);
    }

    private void playWhitsle() {
        MediaPlayer whitsle = MediaPlayer.create(this,R.raw.referee_whistle);
        whitsle.start();
        whitsle.setLooping(false);
    }

    private void Display(int pos) {
        Random rand = new Random();
        SetVisibleAnswer();
        ArrayList<String> listAnswer = new ArrayList<>();

        correctAnswer = quizList.get(pos);
        listAnswer.add(correctAnswer.getName());

        String ImgID = "uncen_" + correctAnswer.getID();

        int src = getResources().getIdentifier(ImgID, "drawable", getPackageName());

        Image.setImageResource(src);
        quizNo.setText("Câu " + String.valueOf(pos + 1));
        //quizCorrect.setText(String.valueOf(res) + "/" + String.valueOf(number));
        //userInfo.setText("Người chơi: " + userName + " — " + "Điểm: " + String.valueOf(score) );

        int i = 0;

        while(i < 3){
            Player wrongAnswer = list.get(rand.nextInt(50));

            if(!CheckDupAnswer(listAnswer, wrongAnswer))
                continue;

            listAnswer.add(wrongAnswer.getName());
            i++;
        }

        Collections.shuffle(listAnswer);

        AnswerA.setText(listAnswer.get(0));
        AnswerB.setText(listAnswer.get(1));
        AnswerC.setText(listAnswer.get(2));
        AnswerD.setText(listAnswer.get(3));

        //Set hint button
        hint.setText("\uD83D\uDCA1 × " + hint_remain);

        if(hint_remain <= 0)
            hint_mode = false;
        else
            hint_mode = true;

        if(hint_mode) {
            hint.setTextColor(Color.parseColor("#202121"));
            hint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPopSound();
                    useHint(correctAnswer);
                }
            });
        }
    }

    private void useHint(Player answer) {
        Random rand = new Random();
        int i = 0;

        while(i<2) {
            switch (rand.nextInt(4)) {
                case 0: {
                    if(AnswerA.getText().equals(answer.getName()))
                        continue;
                    if(AnswerA.getVisibility() == View.INVISIBLE)
                        continue;
                    AnswerA.setVisibility(View.INVISIBLE);
                    break;
                }
                case 1: {
                    if(AnswerB.getText().equals(answer.getName()))
                        continue;
                    if(AnswerB.getVisibility() == View.INVISIBLE)
                        continue;
                    AnswerB.setVisibility(View.INVISIBLE);
                    break;
                }
                case 2: {
                    if(AnswerC.getText().equals(answer.getName()))
                        continue;
                    if(AnswerC.getVisibility() == View.INVISIBLE)
                        continue;
                    AnswerC.setVisibility(View.INVISIBLE);
                    break;
                }
                case 3: {
                    if(AnswerD.getText().equals(answer.getName()))
                        continue;
                    if(AnswerD.getVisibility() == View.INVISIBLE)
                        continue;
                    AnswerD.setVisibility(View.INVISIBLE);
                    break;
                }
            }
            i++;
        }

        hint.setTextColor(Color.parseColor("#6d7070"));
        hint.setOnClickListener(null);
        hint_remain--;
        hint_mode = false;
        hint.setText("\uD83D\uDCA1 × " + hint_remain);
    }

    private Boolean CheckDupAnswer(List<String> listAnswer, Player player) {

        for(int i = 0; i < listAnswer.size(); i++) {
            if(listAnswer.get(i).equals(player.getName()))
                return false;
        }

        return true;
    }

    private void SetVisibleAnswer() {
        AnswerA.setVisibility(View.VISIBLE);
        AnswerB.setVisibility(View.VISIBLE);
        AnswerC.setVisibility(View.VISIBLE);
        AnswerD.setVisibility(View.VISIBLE);
    }

    private void Mapping() {
        Image = findViewById(R.id.ImgPlayer);
        AnswerA = findViewById(R.id.BtnAnswerA);
        AnswerB = findViewById(R.id.BtnAnswerB);
        AnswerC = findViewById(R.id.BtnAnswerC);
        AnswerD = findViewById(R.id.BtnAnswerD);
        layout = findViewById(R.id.layoutEasy);
        quizNo = findViewById(R.id.TxtQuizNo);
        countdown = findViewById(R.id.txtCountdown);
        //quizCorrect = findViewById(R.id.txtCorrectQuiz);
        //userInfo = findViewById(R.id.txtUserInfo);
        hint = findViewById(R.id.hint);
        btnSound = findViewById(R.id.BtnSound);
    }

    private void CreatePlayerList(int number) {
        try {
            String splitBy = ", ";
            FileInputStream in = this.openFileInput("players.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while (br != null) {
                String line = br.readLine();
                if(line == null)
                    break;
                String[] value = line.split(splitBy);
                list.add(new Player(value[0], value[1]));
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(list);

        for(int i = 0; i <= number; i++) {
            quizList.add(list.get(i));
        }
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

    @Override
    protected void onStop() {
        super.onStop();
        countDownTimer.cancel();
        finish();
    }
}