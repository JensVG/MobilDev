package com.example.groupbudget;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.widget.AdapterView;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Group extends AppCompatActivity {

    TextView groupName_textview;
    public String _groupname;
    final List<String> MembersList = new ArrayList<String>();
    final List<String> EventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        //GET GROUPNAME
        groupName_textview = findViewById(R.id.GroupName);
        Bundle bundle = getIntent().getExtras();
        _groupname = bundle.getString("groupname");
        groupName_textview.setText(_groupname);

        //Members
        final Button btn_addMember = findViewById(R.id.addMemberBtn);
        final ListView lv_Members = findViewById(R.id.listview_MemberList);
        final MemberAdapter member_adapter = new MemberAdapter();
        ReadMembers();
        member_adapter.setData(MembersList);
        lv_Members.setAdapter(member_adapter);

        //Events
        final Button btn_addEvent = findViewById(R.id.addEventBtn);
        final ListView lv_Events = findViewById(R.id.listview_EventList);
        final EventAdapter event_adapter = new EventAdapter();
        ReadEvents();
        event_adapter.setData(EventList);
        lv_Events.setAdapter(event_adapter);

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
                                    Toast.makeText(Group.this,"ERROR : empty member name",Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(Group.this,"Member Added!",Toast.LENGTH_SHORT).show();
                                    MembersList.add(memberInput.getText().toString());
                                    member_adapter.setData(MembersList);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
        //Remove Member
        lv_Members.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(Group.this)
                        .setTitle("Delete this Member?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MembersList.remove(position);
                                member_adapter.setData(MembersList);
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();
                return true;
            }
        });
        //Add EventButton
        btn_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText eventInput = new EditText(Group.this);
                eventInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(Group.this )
                        .setTitle("Add a new Event")
                        .setMessage("What is the name of your new Event?")
                        .setView(eventInput)
                        .setPositiveButton("Add Event", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(eventInput.getText().toString().equals(""))
                                    Toast.makeText(Group.this,"ERROR : empty event name",Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(Group.this,"Event Added!",Toast.LENGTH_SHORT).show();
                                    EventList.add(eventInput.getText().toString());
                                    event_adapter.setData(EventList);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
        //Remove Event
        //HOU REKENING MET LIJST, DIE GAAT COMPILCATER ZIJN DAN GEWN MEMEBERS OF GROUPNAME
        lv_Events.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(Group.this)
                        .setTitle("Delete this Event?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventList.remove(position);
                                member_adapter.setData(EventList);
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();
                return true;
            }
        });
        //Go to Event
        lv_Events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getAdapter().getItem(position).toString();
                Log.d("intent", item);

                Intent intent = new Intent(Group.this, Event.class);
                intent.putExtra("eventname", item);
                intent.putStringArrayListExtra("memberslist", member_adapter.getData()); //Send members to event;
                startActivity(intent);
            }
        });
    }
    //METHODS
    @Override
    protected void onPause(){
        super.onPause();
        SaveEvents();
        SaveMembers();

    }
    //SAVE & READ METHODS

    //Members
    private void SaveMembers(){
        try{
            String FilenamePart = _groupname;
            File file = new File(this.getFilesDir(), ( FilenamePart + "_memberlist_file"));

            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fOut));

            for (int i = 0 ; i < MembersList.size() ; i ++){
                writer.write(MembersList.get(i));
                writer.newLine();
            }
            writer.close();
            fOut.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void ReadMembers(){
        File file = new File(this.getFilesDir(), (groupName_textview.getText().toString() + "_memberlist_file"));

        if(!file.exists()){
            Log.d("File", "Memberlist Doesn't exist");
            return;
        }
        try {
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
            String line = reader.readLine();
            while (line != null){
                MembersList.add(line);
                line = reader.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Events
    private void SaveEvents(){
        try{
            String FileNamePart = _groupname;
            File file = new File(this.getFilesDir(), ( FileNamePart + "_eventlist_file"));

            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fOut));

            for (int i = 0 ; i < EventList.size() ; i ++){
                writer.write(EventList.get(i));
                writer.newLine();
            }
            writer.close();
            fOut.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void ReadEvents(){
        File file = new File(this.getFilesDir(), (groupName_textview.getText().toString() + "_eventlist_file"));

        if(!file.exists()){
            Log.d("File", "Eventlist Doesn't exist");
            return;
        }
        try {
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
            String line = reader.readLine();
            while (line != null){
                EventList.add(line);
                line = reader.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //ADAPTERS
    class MemberAdapter extends BaseAdapter {
        List<String> list = new ArrayList<>();
        void setData(List<String> gList){
            list.clear();
            list.addAll(gList);
            notifyDataSetChanged();
        }
        ArrayList<String> getData(){
            ArrayList<String> members = new ArrayList<>();
            members.clear();
            members.addAll(list);
            return members;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) Group.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.member_item, parent, false);
            }

            TextView textview = convertView.findViewById(R.id.itemMember);

            textview.setText(list.get(position));
            return convertView;
        }
    }
    class EventAdapter extends BaseAdapter {
        List<String> list = new ArrayList<>();
        void setData(List<String> gList){
            list.clear();
            list.addAll(gList);
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) Group.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.event_item, parent, false);
            }

            TextView textview = convertView.findViewById(R.id.itemEvent);

            textview.setText(list.get(position));
            return convertView;
        }
    }

}
