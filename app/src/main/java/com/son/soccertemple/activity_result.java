package com.son.soccertemple;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.IntentCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static com.son.soccertemple.R.raw.drump;

public class activity_result extends Activity {

    Button Menu, Replay;
    TextView Name, Score, Result;
    String name;
    int score, result, number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Mapping();
        receivePackage();

        Name.setText(name);
        Score.setText(String.valueOf(score));
        Result.setText(" " + String.valueOf(result) + "/" + String.valueOf(number));
        //Create file csv and save as history
        saveToHistory(name, score, result, number);
        putHistoryToFirestore(name, score, result, number);

        Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopSound();
                Intent intent = new Intent(activity_result.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopSound();
                Intent intent = new Intent(activity_result.this, activity_setup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void setPopSound() {
        MediaPlayer pop = MediaPlayer.create(this,R.raw.pop);
        pop.start();
        pop.setLooping(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void saveToHistory(String name, int score, int result, int number) {
        String fileName = "history.csv";
        String filePath = "/data/data/com.son.soccertemple/files/" + fileName;

        try {
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(name + ", " + score + ", " + result + ", " + number );
            pw.flush();
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void Mapping() {
        Name = findViewById(R.id.TxtName);
        Score = findViewById(R.id.TxtResult);
        Result = findViewById(R.id.TxtRightAnswer2);
        Menu = findViewById(R.id.BtnMenu);
        Replay = findViewById(R.id.BtnPlayAgain);
    }

    void receivePackage() {
        Intent callerIntent = getIntent();
        Bundle packageFromCaller = callerIntent.getBundleExtra("resultPackage");

        name = packageFromCaller.getString("Name");
        score = packageFromCaller.getInt("Score");
        result = packageFromCaller.getInt("NoOfCorrect");
        number = packageFromCaller.getInt("NoOfQuiz");
    }

    void putHistoryToFirestore(String name, int score, int result, int number) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //User newUser = new User(name, String.valueOf(score), String.valueOf(result), String.valueOf(number));

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("Name", name);
        newUser.put("Score", score);
        newUser.put("Result", + result + "/" + number);

        db.collection("History")
                .document()
                .set(newUser);
    }
}
