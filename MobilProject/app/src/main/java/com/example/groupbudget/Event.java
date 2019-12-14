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
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class Event extends AppCompatActivity {

    private Button submitEventBtn;
    private EditText et2;
    private String st2;
    private EditText et3;
    private String st3;
    private EditText et4;
    private String st4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        submitEventBtn = (Button) findViewById(R.id.submitEventBtn);
        et2 = findViewById(R.id.editText2);
        et3 = findViewById(R.id.editText3);
        et4 = findViewById(R.id.editText4);
        submitEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Invullen1();

                Intent i3 = new Intent(Event.this, Group.class);
                st3 = et3.getText().toString();
                i3.putExtra("", st3);
                startActivity(i3);

                finish();

                Intent i4 = new Intent(Event.this, Group.class);
                st4 = et4.getText().toString();
                i4.putExtra("", st4);
                startActivity(i4);

                finish();
            }
        });
    }

    public void Invullen1(){

        Intent i2 = new Intent(Event.this, Group.class);
        st2 = et2.getText().toString();
        i2.putExtra("Momenteel is er nog geen activiteit vast gesteld", st2);
        startActivity(i2);
        finish();
    }
}
