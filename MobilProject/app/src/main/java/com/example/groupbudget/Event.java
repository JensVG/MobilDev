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
                if (et2.getText().toString().equals("") || et3.getText().toString().equals("") || et4.getText().toString().equals("")) {

                    if (et2.getText().toString().equals(""))
                        Toast.makeText(Event.this, "Please add an event name", Toast.LENGTH_SHORT).show();

                    else if (et3.getText().toString().equals(""))
                        Toast.makeText(Event.this, "Please insert number of people", Toast.LENGTH_SHORT).show();

                    else
                        Toast.makeText(Event.this, "Please insert a cost price", Toast.LENGTH_SHORT).show();
                }

                else {
                    int number1 = Integer.parseInt(et3.getText().toString());
                    int number2 = Integer.parseInt(et4.getText().toString());
                    int Price = number2 / number1;
                    Intent i = new Intent(Event.this, Group.class);
                    st2 = et2.getText().toString();
                    st3 = et3.getText().toString();
                    et4.setText(String.valueOf(Price));
                    st4 = et4.getText().toString();

                    i.putExtra("Momenteel is er nog geen activiteit vast gesteld", st2);
                    i.putExtra("aantal", st3);
                    i.putExtra("bedrag", st4);

                    Toast.makeText(Event.this,"Event Added!", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                    //startActivityForResult(i,1);
                }
            }
        });
    }
}
