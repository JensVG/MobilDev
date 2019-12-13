package com.example.groupbudget;

import android.content.Intent;
import android.os.Bundle;

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

public class Pop extends AppCompatActivity {

    private Button submitGroupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        submitGroupBtn = (Button) findViewById(R.id.submitGroupBtn);
        submitGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openAddGroupScreen();
            }
        });
    }
    public void openAddGroupScreen(){
        Intent intent = new Intent(this,Group.class);
        startActivity(intent);
    }
}