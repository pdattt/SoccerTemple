package com.son.soccertemple.quizDif;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

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
    //private Stack<Player> playerQuiz = new Stack<>();
    ImageView Image;
    Button AnswerA, AnswerB, AnswerC, AnswerD;
    int pos = 0;
    int res = 0;
    int score = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_easy);

        Mapping();
        CreatePlayerList();
        Collections.shuffle(list);

        //Receive bundle package
        //Create a list with a parameter is the number of the quiz
        int number = 5;

        Display(pos);
    }


    private void Display(int pos) {
        Random rand = new Random();
        ArrayList<String> listAnswer = new ArrayList<>();

        Player correctAnswer = new Player();
        correctAnswer = list.get(pos);
        listAnswer.add(correctAnswer.getName());


        int i = 0;
        while(i < 3){
            Player wrongAnswer = list.get(rand.nextInt(50));

            if(!CheckDupAnswer(listAnswer))
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

    private Boolean CheckDupAnswer(List<String> listAnswer) {

        for(int i = 0; i < listAnswer.size(); i++) {
            for(int j = i + 1; j < listAnswer.size(); j++) {
                if(listAnswer.get(i).equals(listAnswer.get(j)))
                    return false;
            }
        }

        return true;
    }

    private void Mapping() {
        Image = (ImageView)findViewById(R.id.ImgPlayer);
        AnswerA = (Button)findViewById(R.id.BtnAnswerA);
        AnswerB = (Button)findViewById(R.id.BtnAnswerB);
        AnswerC = (Button)findViewById(R.id.BtnAnswerC);
        AnswerD = (Button)findViewById(R.id.BtnAnswerD);
    }


    private void CreatePlayerList() {
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
    }

}


