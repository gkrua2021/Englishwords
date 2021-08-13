package com.example.englishword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class papagohelp extends AppCompatActivity {

    static TextView tv1, tv2, tv3, tv4, tv5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papagohelp);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
    }
}