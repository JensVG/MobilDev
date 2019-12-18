package com.example.groupbudget;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
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
import android.widget.Toast;

import org.w3c.dom.Text;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Group extends AppCompatActivity {
    //TEMPORARY
    private Button addEventBtn;
    private Button goBackBtn;
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

    //FIELDS
    private String _groupname;
    final List<String> MembersList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        //GET GROUPNAME
        _groupname = getIntent().getStringExtra(MainActivity.GROUPNAME);
        final TextView groupName_textview = findViewById(R.id.GroupName);
        groupName_textview.setText(_groupname);

        //Members
        final Button btn_addMember = findViewById(R.id.add_member_btn);
        final ScrollView scrl_members = findViewById(R.id.scrollableMembers);
        final TextView txt_placeholderMembers = findViewById(R.id.txt_EmptyMembersPlaceHolder);
        final ListView listview_Members = findViewById(R.id.listview_MemberList);
        final MemberAdapter member_adapter = new MemberAdapter();

        //Read Group Items from file
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

        //Add MemberButton
        btn_addMember.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final EditText memberInput = new EditText(Group.this);
                memberInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(Group.this )
                        .setTitle("Add a new Member")
                        .setMessage("What is the name of your new member?")
                        .setView(memberInput)
                        .setPositiveButton("Add Member", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(memberInput.getText().toString().equals(""))
                                    Toast.makeText(Group.this,"ERROR : empty group name",Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(Group.this,"Group Added!",Toast.LENGTH_SHORT).show();
                                    MembersList.add(memberInput.getText().toString());
                                    member_adapter.setData(MembersList);
                                    //adapter.writeData(GroupNamesList);
                                    scrl_members.setVisibility(View.VISIBLE);
                                    txt_placeholderMembers.setVisibility(View.GONE);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });



        tv = findViewById(R.id.GroupName);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);

        st = getIntent().getExtras().getString("Value");
        st2 = getIntent().getExtras().getString("Momenteel is er nog geen activiteit vast gesteld");
        st3 = getIntent().getExtras().getString("aantal");
        st4 = getIntent().getExtras().getString("bedrag");

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

        goBackBtn =(Button)findViewById(R.id.goBackBtn);
        goBackBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View view){
                Intent i = new Intent(Group.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    class MemberAdapter extends BaseAdapter {
        List<String> list = new ArrayList<>();
        String MemberName = new String();
        void setData(List<String> gList){
            list.clear();
            list.addAll(gList);
            notifyDataSetChanged();

        }
        void writeData(List<String> gList){

            for (String names : gList){
                MemberName += names + ",";
            }
            //Write to file
            try{
                FileOutputStream fileout=openFileOutput(groupName_textview.getText().toString() +"_memberlist_file.txt", MODE_PRIVATE);
                OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                outputWriter.write(MemberName);
                outputWriter.close();
                Toast.makeText(getBaseContext(), "Member Added!",Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                Toast.makeText(getBaseContext(), "Member Not Added!",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) Group.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.group_item, parent, false);
            }

            TextView textview = convertView.findViewById(R.id.itemGroup);

            textview.setText(list.get(position));
            return convertView;
        }
    }
}
