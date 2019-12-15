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

        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.PopupWindow;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.w3c.dom.Text;

        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.util.ArrayList;
        import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<String>GroupNamesList = new ArrayList<>();
    //public static final String GROUP_NAMES=
            //"com.example.android.groupbudget.extra.GROUPNAMES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView placeHolder = findViewById(R.id.txt_EmptyGroupPlaceHolder);
        final ListView groups_listview =  findViewById(R.id.listview_GroupList);
        final Button addGroupBtn = findViewById(R.id.addGroupBtn);
        final GroupAdapter adapter = new GroupAdapter();


        //Read groupnames
        adapter.setData(GroupNamesList);
        groups_listview.setAdapter(adapter);

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
                                GroupNamesList.add(groupnameInput.getText().toString());
                                adapter.setData(GroupNamesList);
                                //adapter.writeData(GroupNamesList);
                                placeHolder.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });
    }
    class GroupAdapter extends BaseAdapter{
        List<String> list = new ArrayList<>();
        String GroupNames = new String();
        void setData(List<String> gList){
            list.clear();
            list.addAll(gList);
            notifyDataSetChanged();

        }
        void writeData(List<String> gList){

            for (String names : gList){
                GroupNames += names + ",";
            }
            //Write to file
            try{
                FileOutputStream fileout=openFileOutput("groupnamefile.txt", MODE_PRIVATE);
                OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                outputWriter.write(GroupNames);
                outputWriter.close();
                Toast.makeText(getBaseContext(), "Group Added!",Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                Toast.makeText(getBaseContext(), "Group Not Added!",Toast.LENGTH_SHORT).show();
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
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.group_item, parent, false);
            TextView textview = rowView.findViewById(R.id.itemGroup);

            textview.setText(list.get(position));
            return rowView;
        }
    }
}