package com.son.soccertemple;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;


public class activity_highscore extends Activity {
    ListView listHistory;
    LinearLayout layout;
    Button back;
    RadioButton local, global;
    ArrayList<User> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        Mapping();
        getHistoryData();
        printHistory();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void printHistory() {
        ArrayList<String> history = new ArrayList<>();
        int i, j;

        for(i = 0; i < list.size() - 2; i++)
            for(j = i + 1; j <list.size() - 2; j++) {
                if (list.get(i).getScore() < list.get(j).getScore()) {
                    User userTemp = list.get(j);
                    list.set(j, list.get(i));
                    list.set(i, userTemp);
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

    private void Mapping() {
        listHistory = findViewById(R.id.listHighscore);
        layout = findViewById(R.id.highscoreLayout);
        back = findViewById(R.id.btnBack);
        local = findViewById(R.id.radioLocal);
        global = findViewById(R.id.radioGlobal);
    }

    private void getHistoryData() {
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
    }
}
