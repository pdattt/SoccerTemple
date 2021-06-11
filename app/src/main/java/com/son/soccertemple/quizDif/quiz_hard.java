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
import android.widget.EditText;
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
import java.util.Random;

public class quiz_hard extends Activity {
    private ArrayList<Player> list = new ArrayList<>();
    private ArrayList<Player> quizList = new ArrayList<>();
    //private Stack<Player> playerQuiz = new Stack<>();
    ImageView Image;
    Button Submit;
    Player correctAnswer;
    LinearLayout layout;
    TextView quizNo, error, countdown, hintText, hint;
    EditText Answer;
    Intent svc;
    ImageButton btnSound;

    int pos = 0;
    int res = 0;
    int score = 0;
    int point = 90;
    int number;
    int timeCountdown = 15;
    int hint_remain = 3;
    boolean hint_mode = true;
    int music_mode = 1;
    String userName;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_hard);

        Mapping();
        svc = new Intent(this, BackgroundService.class);

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

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopSound();
                if (Answer.getText().toString().trim().equals("")) {
                    error.setVisibility(View.VISIBLE);
                }
                else {
                    final Intent intent = new Intent(quiz_hard.this, activity_result.class);

                    if (Answer.getText().toString().trim().toLowerCase().equals(correctAnswer.getName().trim().toLowerCase())) {
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

                    Answer.setText("");
                    Answer.setHint("Đáp án");
                }
            }
        });
    }

    private void Display(int pos) {

        correctAnswer = quizList.get(pos);
        String ImgID = "uncen_" + correctAnswer.getID();
        error.setVisibility(View.INVISIBLE);
        hintText.setText("");

        int src = getResources().getIdentifier(ImgID, "drawable", getPackageName());

        Image.setImageResource(src);
        quizNo.setText("Câu " + (pos + 1));

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
                    hintText.setText(useHint(correctAnswer));
                }
            });
        }
    }

    private String useHint(Player answer) {
        Random rand = new Random();
        String playerName = answer.getName();
        StringBuilder hintName = new StringBuilder(playerName);

        // Pick a number of characters from the answer to hide
        int number = (int) (playerName.trim().length() * 0.4);

        for(int i = 0; i < number; i++) {
            int randNum = rand.nextInt(playerName.length());

            if(hintName.charAt(randNum) == '∙' || hintName.charAt(randNum) == ' ')
                continue;

            hintName.setCharAt(randNum, '∙');
        }

        hint.setTextColor(Color.parseColor("#6d7070"));
        hint.setOnClickListener(null);
        hint_remain--;
        hint_mode = false;
        hint.setText("\uD83D\uDCA1 × " + hint_remain);

        return hintName.toString();
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

    private void setPopSound() {
        MediaPlayer pop = MediaPlayer.create(this,R.raw.pop);
        pop.start();
        pop.setLooping(false);
    }

    private void FinishQuiz() {
        final Intent intent = new Intent(quiz_hard.this, activity_result.class);

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

    private void Mapping() {
        Image = findViewById(R.id.ImgPlayer);
        Submit = findViewById(R.id.btnSubmit);
        quizNo = findViewById(R.id.TxtQuizNo);
        Answer = findViewById(R.id.EdtAnswer);
        error = findViewById(R.id.TxtErrorAnswer);
        layout = findViewById(R.id.layoutHard);
        countdown = findViewById(R.id.txtCountdown);
        hint = findViewById(R.id.hint);
        hintText = findViewById(R.id.hintText);
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