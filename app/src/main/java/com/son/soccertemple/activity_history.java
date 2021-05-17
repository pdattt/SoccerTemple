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
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class activity_history extends Activity {
    ListView listHistory;
    LinearLayout layout;
    Button back;
    ArrayList<User> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

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

        for(int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            history.add(user.toString());
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
        listHistory = findViewById(R.id.listHistory);
        layout = findViewById(R.id.historyLayout);
        back = findViewById(R.id.btnBack);
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
