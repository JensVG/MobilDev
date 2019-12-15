package com.example.groupbudget;

        import android.content.Context;
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
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.PopupWindow;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import java.io.FileInputStream;
        import java.io.InputStreamReader;
        import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView placeHolder;
    ListView groups;
    String[] GroupNamesList;
    LayoutInflater ListViewInflator;
    private Button addGroupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placeHolder = (TextView) findViewById(R.id.txt_EmptyGroupPlaceHolder);
        groups = (ListView) findViewById(R.id.listview_GroupList);
        addGroupBtn = (Button) findViewById(R.id.addGroupBtn);


        //Read groupnames
        try{
            FileInputStream fileIn=openFileInput("groupnamefile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputbuffer = new char[200];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputbuffer)) > 0){
                String readString=String.copyValueOf(inputbuffer,0,charRead);
                s+=readString;
            }
            InputRead.close();
            if (s == ""){ //AANPASSEN DAT VISIBLE WORDT ALS GROUPLIJST LEEG IS
                placeHolder.setVisibility(View.VISIBLE);
            }
            else{
                placeHolder.setVisibility(View.GONE);
                GroupNamesList = s.split(",");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        addGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openAddGroupScreen();
            }
        });
    }
    public void openAddGroupScreen(){
        Intent intent = new Intent(this,Pop.class);
        startActivity(intent);
    }
}