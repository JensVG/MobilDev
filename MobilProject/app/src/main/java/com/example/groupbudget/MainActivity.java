package com.example.groupbudget;

        import android.app.Dialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;

        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.android.material.snackbar.Snackbar;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;

        import android.telephony.SmsManager;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.PopupWindow;
        import android.widget.RelativeLayout;
        import android.widget.ScrollView;
        import android.widget.TextView;
        import android.widget.Toast;
        import org.w3c.dom.Text;

        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.nio.Buffer;
        import java.nio.file.attribute.PosixFileAttributes;
        import java.util.ArrayList;
        import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<String>GroupNamesList = new ArrayList<>();
    private TextView placeHolder ;
    private ScrollView scrollView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            placeHolder = findViewById(R.id.txt_EmptyGroupPlaceHolder);
            scrollView = findViewById(R.id.scrollable);
            final ListView lv_grouplist =  findViewById(R.id.listview_GroupList);
            final Button addGroupBtn = findViewById(R.id.addGroupBtn);
            final GroupAdapter adapter = new GroupAdapter();

            //Read groupnames
            ReadGroups();
            CheckEmptyList();
            adapter.setData(GroupNamesList);
            lv_grouplist.setAdapter(adapter);
            //Add Group
            addGroupBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    final EditText groupnameInput = new EditText(MainActivity.this);
                    groupnameInput.setSingleLine();
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this )
                            .setTitle("Add a new Group")
                            .setMessage("What is the name of your group?")
                            .setView(groupnameInput)
                            .setPositiveButton("Add Group", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(groupnameInput.getText().toString().equals(""))
                                        Toast.makeText(MainActivity.this,"ERROR : empty group name",Toast.LENGTH_SHORT).show();
                                    else{
                                        Toast.makeText(MainActivity.this,"Group Added!",Toast.LENGTH_SHORT).show();
                                        GroupNamesList.add(groupnameInput.getText().toString());
                                        adapter.setData(GroupNamesList);
                                        CheckEmptyList();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .create();
                    dialog.show();
                }
            });
        //Delete Group
        lv_grouplist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete this group?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GroupNamesList.remove(position);
                                adapter.setData(GroupNamesList);
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();
                return true;
            }
        });
        //Go to Group
        lv_grouplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getAdapter().getItem(position).toString();//GET ITEM OUT OF LISTVIEW
                Log.d("intent", item);
              Intent intent = new Intent(MainActivity.this, Group.class);
              intent.putExtra("groupname", item);

              startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        SaveGroups();
        CheckEmptyList();
    }
    private void CheckEmptyList(){
        if (GroupNamesList.isEmpty()){
            scrollView.setVisibility(View.GONE);
            placeHolder.setVisibility(View.VISIBLE);
        }
        else {
            scrollView.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);
        }
    }
    private void SaveGroups(){
        try{
            File file = new File(this.getFilesDir(), "groupnames");

            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fOut));

            for (int i = 0 ; i < GroupNamesList.size() ; i ++){
                writer.write(GroupNamesList.get(i));
                writer.newLine();
            }
            writer.close();
            fOut.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void ReadGroups(){
        File file = new File(this.getFilesDir(), "groupnames");

        if(!file.exists()){
            return;
        }
        try {
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
            String line = reader.readLine();
            while (line != null){
                GroupNamesList.add(line);
                line = reader.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    class GroupAdapter extends BaseAdapter{
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

                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.group_item, parent, false);
            }
            TextView textview = convertView.findViewById(R.id.itemGroup);
            textview.setText(list.get(position));
            return convertView;
        }
    }
}