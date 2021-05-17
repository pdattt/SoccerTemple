package com.example.highscore;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    Button Test,HighScore, Quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Test =(Button)findViewById(R.id.BtnTest);
        Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, activity_test.class);
                startActivity(intent);
            }
        });
        HighScore =(Button)findViewById(R.id.BtnHighScore);
        HighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_highscore.class);
                startActivity(intent);
            }
        });
        Quit =(Button)findViewById(R.id.BtnThoat);
        Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ///Import data to csv
        //int score, result,number,id;
        //
//        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
//        String fileName = "Highscore.csv";
//        String filePath = baseDir + File.separator + Highscore.csv;
//        File f = new File(filePath);
//        CSVWriter writer;
//
//        // File exist
//        if(f.exists()&&!f.isDirectory())
//        {
//            mFileWriter = new FileWriter(filePath, true);
//            writer = new CSVWriter(mFileWriter);
//        }
//        else
//        {
//            writer = new CSVWriter(new FileWriter(filePath));
//        }
//
//        String[] data = {"Id id", "Name Name", "Score score", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").formatter.format(date)});
//
//        writer.writeNext(data);
//
//        writer.close();
    }
}