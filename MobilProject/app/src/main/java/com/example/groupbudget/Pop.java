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
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Pop extends AppCompatActivity {

    private Button submitGroupBtn;
    private EditText et;
    private String st;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        submitGroupBtn = (Button) findViewById(R.id.submitGroupBtn);
        et = findViewById(R.id.editText);
        submitGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Write to file
                try{
                    FileOutputStream fileout=openFileOutput("groupnamefile.txt", MODE_PRIVATE);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                    outputWriter.write(et.getText().toString() + ",");
                    outputWriter.close();

                    //Display file saved message
                    Toast.makeText(getBaseContext(), "Group Added!",Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                if(et.getText().toString().equals(""))
                  Toast.makeText(Pop.this,"Please add an group name", Toast.LENGTH_SHORT).show();

                else{
                Intent i = new Intent(Pop.this, Group.class);
                st = et.getText().toString();
                i.putExtra("Value", st);
                startActivity(i);
                finish();
            }
            }
        });
    }
}