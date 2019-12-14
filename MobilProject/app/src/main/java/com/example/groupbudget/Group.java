package com.example.groupbudget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.widget.TextView;

public class Group extends AppCompatActivity {
     TextView tv;
     String st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    tv = findViewById(R.id.textView);
    st = getIntent().getExtras().getString("Value");
    tv.setText(st);
    }
}
