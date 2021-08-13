package com.example.englishword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class word_management extends AppCompatActivity {

    Button bt1, bt2, bt3, bt4, bt5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_management);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
    }
}