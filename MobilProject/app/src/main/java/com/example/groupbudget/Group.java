package com.example.groupbudget;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.widget.TextView;
import android.content.Intent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class Group extends AppCompatActivity {
    private Button addEventBtn;
     TextView tv;
     String st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        tv = findViewById(R.id.textView);
        st = getIntent().getExtras().getString("Value");
        tv.setText(st);

        addEventBtn = (Button) findViewById(R.id.addEventBtn);
        addEventBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openAddGroupScreen();
            }
        });
    }
    public void openAddGroupScreen(){
        Intent intent = new Intent(this,Event.class);
        startActivity(intent);
    }
}
