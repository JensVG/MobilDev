package com.example.groupbudget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class View_Group extends AppCompatActivity {
    static final int READ_BLOCK_SIZE = 100;
    TextView groupName_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__group);
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
    }
}
