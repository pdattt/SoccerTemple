package com.son.soccertemple.quizDif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

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

public class quiz_hard extends Activity {
    private ArrayList<Player> list = new ArrayList<>();
    private ArrayList<Player> quizList = new ArrayList<>();
    //private Stack<Player> playerQuiz = new Stack<>();
    ImageView Image;
    Button Submit;
    Player correctAnswer;
    LinearLayout layout;
    TextView quizNo, error, countdown;
    EditText Answer;

    int pos = 0;
    int res = 0;
    int score = 0;
    int point = 90;
    int number;
    int timeCountdown = 15;
    String userName;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_hard);

        Mapping();

        //Receive bundle package
        //Create a list with a parameter is the number of the quiz
        Intent callerIntent = getIntent();
        Bundle packageFromCaller = callerIntent.getBundleExtra("setupPackage");

        number = packageFromCaller.getInt("quizNum");
        userName = packageFromCaller.getString("userName");
        CreatePlayerList(number);

        Display(pos);
        startCountdownTimer();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Answer.getText().toString().trim().equals("")) {
                    error.setVisibility(View.VISIBLE);
                }
                else {
                    final Intent intent = new Intent(quiz_hard.this, activity_result.class);
                    error.setVisibility(View.INVISIBLE);

                    if (Answer.getText().toString().trim().toLowerCase().equals(correctAnswer.getName().trim().toLowerCase())) {
                        res++;
                        score += point;
                    }

                    pos++;

                    if (pos >= number)
                        FinishQuiz();

                    Display(pos);
                    countDownTimer.cancel();
                    startCountdownTimer();

                    Answer.setText("");
                    Answer.setHint("Đáp án");
                }
            }
        });
    }

    private void Display(int pos) {
        ArrayList<String> listAnswer = new ArrayList<>();

        correctAnswer = quizList.get(pos);
        listAnswer.add(correctAnswer.getName());

        String ImgID = "uncen_" + correctAnswer.getID();

        int src = getResources().getIdentifier(ImgID, "drawable", getPackageName());

        Image.setImageResource(src);
        quizNo.setText("Câu " + (pos + 1));

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
                if(pos >= number)
                    FinishQuiz();

                Display(pos);
                startCountdownTimer();
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

    private void FinishQuiz() {
        final Intent intent = new Intent(quiz_hard.this, activity_result.class);

        layout.removeAllViews();
        //pass bundle package
        Bundle bundle = new Bundle();
        bundle.putString("Name", userName);
        bundle.putInt("Score", score);
        bundle.putInt("NoOfQuiz", number);
        bundle.putInt("NoOfCorrect", res);
        intent.putExtra("resultPackage", bundle);
        //start result Activity
        startActivity(intent);
        finish();
    }

    private void Mapping() {
        Image = findViewById(R.id.ImgPlayer);
        Submit = findViewById(R.id.btnSubmit);
        quizNo = findViewById(R.id.TxtQuizNo);
        //quizCorrect = findViewById(R.id.txtCorrectQuiz);
        //userInfo = findViewById(R.id.txtUserInfo);
        Answer = findViewById(R.id.EdtAnswer);
        error = findViewById(R.id.TxtErrorAnswer);
        layout = findViewById(R.id.layoutHard);
        countdown = findViewById(R.id.txtCountdown);
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
}