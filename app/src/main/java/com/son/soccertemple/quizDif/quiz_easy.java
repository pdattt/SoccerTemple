package com.son.soccertemple.quizDif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.List;
import java.util.Random;

public class quiz_easy extends Activity {
    private ArrayList<Player> list = new ArrayList<>();
    private ArrayList<Player> quizList = new ArrayList<>();
    //private Stack<Player> playerQuiz = new Stack<>();
    ImageView Image;
    Button AnswerA, AnswerB, AnswerC, AnswerD;
    Player correctAnswer;
    LinearLayout layout;
    TextView quizNo, quizCorrect, userInfo;

    int pos = 0;
    int res = 0;
    int score = 0;
    int point = 50;
    int number;
    String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_easy);

        Mapping();

        //Receive bundle package
        //Create a list with a parameter is the number of the quiz
        Intent callerIntent = getIntent();
        Bundle packageFromCaller = callerIntent.getBundleExtra("setupPackage");

        number = packageFromCaller.getInt("quizNum");
        userName = packageFromCaller.getString("userName");
        CreatePlayerList(number);

        Display(pos);
        setAnswerOnClick(AnswerA, number);
        setAnswerOnClick(AnswerB, number);
        setAnswerOnClick(AnswerC, number);
        setAnswerOnClick(AnswerD, number);
    }

    public void setAnswerOnClick(final Button BT, final int number) {
        final Intent intent = new Intent(quiz_easy.this, activity_result.class);

        BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BT.getText().toString().equals(correctAnswer.getName())) {
                    res++;
                    score += point;
                }
                pos++;
                if(pos >= number) {
                    layout.removeAllViews();
                    userInfo.setVisibility(View.INVISIBLE);
                    //pass bundle package
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", userName);
                    bundle.putInt("Score", score);
                    bundle.putInt("NoOfQuiz", number);
                    bundle.putInt("NoOfCorrect", res);
                    intent.putExtra("resultPackage", bundle);
                    //start result Activity
                    startActivity(intent);
                }
                Display(pos);
            }
        });
    }

    private void Display(int pos) {
        Random rand = new Random();
        ArrayList<String> listAnswer = new ArrayList<>();

        correctAnswer = quizList.get(pos);
        listAnswer.add(correctAnswer.getName());

        String ImgID = "uncen_" + correctAnswer.getID().toString();

        int src = getResources().getIdentifier(ImgID, "drawable", getPackageName());

        Image.setImageResource(src);
        quizNo.setText("Câu " + String.valueOf(pos + 1));
        quizCorrect.setText(String.valueOf(res) + "/" + String.valueOf(number));
        userInfo.setText("Người chơi: " + userName + " — " + "Điểm: " + String.valueOf(score) );

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
    }

    private Boolean CheckDupAnswer(List<String> listAnswer, Player player) {

        for(int i = 0; i < listAnswer.size(); i++) {
            if(listAnswer.get(i).equals(player.getName()))
                return false;
        }

        return true;
    }

    private void Mapping() {
        Image = findViewById(R.id.ImgPlayer);
        AnswerA = findViewById(R.id.BtnAnswerA);
        AnswerB = findViewById(R.id.BtnAnswerB);
        AnswerC = findViewById(R.id.BtnAnswerC);
        AnswerD = findViewById(R.id.BtnAnswerD);
        layout = findViewById(R.id.layoutEasy);
        quizNo = findViewById(R.id.TxtQuizNo);
        quizCorrect = findViewById(R.id.txtCorrectQuiz);
        userInfo = findViewById(R.id.txtUserInfo);
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