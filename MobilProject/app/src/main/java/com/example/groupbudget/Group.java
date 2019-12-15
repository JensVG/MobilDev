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
import org.w3c.dom.Text;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Group extends AppCompatActivity {
    private Button addEventBtn;
    private TextView tv;
    private String st;
    private TextView tv2;
    private String st2;
    private TextView tv3;
    private String st3;
    private TextView tv4;
    private String st4;
    static final int READ_BLOCK_SIZE = 100;
    TextView groupName_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        groupName_textview = (TextView)findViewById(R.id.GroupName);
        //Read Group name from file
        try{
            FileInputStream fileIn=openFileInput("groupnamefile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while((charRead=InputRead.read(inputBuffer)) > 0){
                String readstring = String.copyValueOf(inputBuffer,0,charRead);
                s+=readstring;
            }
            InputRead.close();
            groupName_textview.setText(s);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        tv = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);
        st = getIntent().getExtras().getString("Value");
        st2 = getIntent().getExtras().getString("Momenteel is er nog geen activiteit vast gesteld");
        st3 = getIntent().getExtras().getString("");
        st4 = getIntent().getExtras().getString("");
        tv.setText(st);
        tv2.setText(st2);
        tv3.setText(st3);
        tv4.setText(st4);
        addEventBtn = (Button) findViewById(R.id.addEventBtn);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Group.this, Event.class);
                startActivity(i);
                finish();
            }
        });
    }
}
