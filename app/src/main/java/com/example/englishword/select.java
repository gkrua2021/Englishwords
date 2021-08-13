package com.example.englishword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class select extends AppCompatActivity {

    static EditText et1, et2;
    static Button bt1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        bt1 = findViewById(R.id.bt1);
    }
}