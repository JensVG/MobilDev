package com.example.groupbudget;

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
        import java.util.ArrayList;
        import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<String>GroupNamesList = new ArrayList<>();
    public static final String GROUPNAME=
            "com.example.android.groupbudget.extra.GROUPNAMES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView groups_listview =  findViewById(R.id.listview_GroupList);
        final Button addGroupBtn = findViewById(R.id.addGroupBtn);
        final GroupAdapter adapter = new GroupAdapter();
        final TextView placeHolder = findViewById(R.id.txt_EmptyGroupPlaceHolder);
        final ScrollView scrollView = findViewById(R.id.scrollable);

        //Read groupnames
        ReadGroups();
        if(!GroupNamesList.isEmpty()){
            scrollView.setVisibility(View.VISIBLE);
            placeHolder.setVisibility(View.GONE);
        }
        adapter.setData(GroupNamesList);
        groups_listview.setAdapter(adapter);
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

                                    scrollView.setVisibility(View.VISIBLE);
                                    placeHolder.setVisibility(View.GONE);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
        //Go to Group
        groups_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
              final TextView item = (TextView) groups_listview.findFocus();
              final String item_groupname = item.getText().toString();


              Intent intent = new Intent(MainActivity.this,Group.class);
              intent.putExtra("GROUPNAME", item_groupname);
              startActivity(intent);
        }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        SaveGroups();
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
        String GroupNames = new String();
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
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.group_item, parent, false);
            TextView textview = rowView.findViewById(R.id.itemGroup);

            textview.setText(list.get(position));
            return rowView;
        }
    }
}