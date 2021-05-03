package com.son.soccertemple.quizDif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.son.soccertemple.Player;
import com.son.soccertemple.R;

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
    int pos = 0;
    int res = 0;
    int score = 0;
    int point = 50;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_easy);

        Mapping();

        //Receive bundle package
        //Create a list with a parameter is the number of the quiz
        int number = 5;

        CreatePlayerList(number);

        Display(pos);
        onClick(number);
    }

    public void onClick(final int number) {

        final Intent intent;

        AnswerA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AnswerA.getText().toString().equals(correctAnswer.getName())) {
                    res++;
                    score += point;
                }
                pos++;
                if(pos >= number) {
                    layout.removeAllViews();
                    //Add res and score to bundle package
                    //start result Activity
                }
                Display(pos);
            }
        });

        AnswerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AnswerB.getText().toString().equals(correctAnswer.getName())) res++;
                pos++;
                if(pos >= number)
                    finish();
                Display(pos);
            }
        });

        AnswerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AnswerC.getText().toString().equals(correctAnswer.getName())) res++;
                pos++;
                if(pos >= number)
                    finish();
                Display(pos);
            }

        });AnswerD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AnswerD.getText().toString().equals(correctAnswer.getName())) res++;
                pos++;
                if(pos >= number)
                    finish();
                Display(pos);
            }
        });
        //if(pos >= number)
    }

    private void Display(int pos) {
        Random rand = new Random();
        ArrayList<String> listAnswer = new ArrayList<>();

        correctAnswer = quizList.get(pos);
        listAnswer.add(correctAnswer.getName());

        String ImgID = "uncen_" + correctAnswer.getID().toString();

        int src = getResources().getIdentifier(ImgID, "drawable", getPackageName());

        Image.setImageResource(src);

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


