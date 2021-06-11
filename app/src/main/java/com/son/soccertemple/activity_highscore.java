package com.son.soccertemple;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class activity_highscore extends Activity {
    ListView listHistory;
    LinearLayout layout;
    Button back;
    RadioGroup radioGroup;
    RadioButton local, global;
    ArrayList<User> list = new ArrayList<>();
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        db = FirebaseFirestore.getInstance();
        Mapping();
        local.setChecked(true);
        getHistoryData();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            setPopSound();
            if(local.isChecked())
                getHistoryData();

            if(global.isChecked())
                getCloudData();
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopSound();
                finish();
            }
        });
    }

    private void printHistory() {
        ArrayList<String> history = new ArrayList<>();
        int i, j;

        if(list.size() >= 2) {
            for (i = 0; i < list.size() - 1; i++)
                for (j = i + 1; j < list.size(); j++) {
                    if (list.get(i).getScore() < list.get(j).getScore()) {
                        User userTemp = list.get(j);
                        list.set(j, list.get(i));
                        list.set(i, userTemp);
                    }
                }
        }

        int count = 0;

        for(i = 0; i < list.size(); i++) {
            if(count >= 10)
                break;

            User user = list.get(i);
            history.add(user.toString());
            count++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, history){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

                // Return the view
                return view;
            }
        };

        listHistory.setAdapter(adapter);
    }

    private void setPopSound() {
        MediaPlayer pop = MediaPlayer.create(this,R.raw.pop);
        pop.start();
        pop.setLooping(false);
    }

    private void Mapping() {
        listHistory = findViewById(R.id.listHighscore);
        layout = findViewById(R.id.highscoreLayout);
        back = findViewById(R.id.btnBack);
        local = findViewById(R.id.radioLocal);
        global = findViewById(R.id.radioGlobal);
        radioGroup = findViewById(R.id.radioGroup);
    }

    private void getHistoryData() {
        list.clear();
        try {
            String splitBy = ", ";
            FileInputStream in = this.openFileInput("history.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while (br != null) {
                String line = br.readLine();
                if(line == null)
                    break;
                String[] value = line.split(splitBy);
                list.add(new User(value[0], value[1], value[2], value[3]));
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printHistory();
    }

    private void getCloudData() {
        list.clear();

        db.collection("History").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> docList = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : docList) {
                                User user = new User();
                                user.setName(d.get("Name").toString());
                                user.setScore(d.get("Score").toString());
                                String[] split = String.valueOf(d.get("Result")).split("/");
                                user.setResult(split[0]);
                                user.setNumber(split[1]);

                                list.add(user);
                            }
                            printHistory();
                        }
                    }
                });
        }
    }
