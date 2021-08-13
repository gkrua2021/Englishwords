package com.example.englishword;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class main_page extends AppCompatActivity {

    EditText et1;
    TextView tv1, tv2;
    Button bt1, bt2, bt3, bt4;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        et1 = findViewById(R.id.et1);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        listView = findViewById(R.id.listView);
    }

}