package com.example.groupbudget;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Event extends AppCompatActivity {

    final List<String>CostList = new ArrayList<>(); //Loopt gelijk met PayersList
    final List<String>CostNameList = new ArrayList<>();
    final List<Double> AmountToPayList = new ArrayList<Double>(); //Loopt gelijk met GroupsMembers
    final List<String> PayerList = new ArrayList<>(); //Loopt gelijk met CostList
    final List<String> PayPlanList = new ArrayList<>();
    private List<String> GroupMembers = new ArrayList<>();
    private String _groupname, _eventname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //INTENT
        Bundle bundle = getIntent().getExtras();
        _groupname = bundle.getString("groupname");
        GroupMembers = bundle.getStringArrayList("memberslist");
        _eventname = bundle.getString("eventname");

        final TextView EvenName = findViewById(R.id.EventName);
        EvenName.setText(_eventname);

        //Payments
        final Button btn_addCost = findViewById(R.id.addPaymentBtn);
        final ListView lv_Cost = findViewById(R.id.listview_PayList);
        final CostAdapter cost_adapter = new CostAdapter();
        ReadPayments();
        cost_adapter.setData(CostNameList);
        lv_Cost.setAdapter(cost_adapter);

        //PaymentPlan
        final Button btn_calculate = findViewById(R.id.btn_CalculatePayment);
        final ListView lv_Payplan = findViewById(R.id.listview_PaymentPlan);
        final PaymentPlanAdapter payplan_adapter = new PaymentPlanAdapter();
        ReadPaymentPlan();
        payplan_adapter.setData(PayPlanList);
        lv_Payplan.setAdapter(payplan_adapter);


        //Add Payment
        btn_addCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText costNameInput = new EditText(Event.this);
                final EditText amountInput = new EditText(Event.this);
                final CharSequence[] people = GroupMembers.toArray(new CharSequence[0]);

                costNameInput.setSingleLine();
                AlertDialog dialog_costName = new AlertDialog.Builder(Event.this )
                        .setTitle("Name of the cost")
                        .setView(costNameInput)

                        .setPositiveButton("Add cost name", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(costNameInput.getText().toString().equals(""))
                                    Toast.makeText(Event.this,"ERROR : empty cost name",Toast.LENGTH_SHORT).show();
                                else{
                                    CostNameList.add(costNameInput.getText().toString());
                                    cost_adapter.setData(CostNameList);// CHANGE TO COSTNAME ADAPTER
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog_costName.show();

                AlertDialog dialog_amount = new AlertDialog.Builder(Event.this)
                        .setTitle("Give the amount of money you spent: ")
                        .setView(amountInput)

                        .setPositiveButton("Add Cost", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (amountInput.getText().toString().equals(""))
                                    Toast.makeText(Event.this,"ERROR : empty Amount name",Toast.LENGTH_SHORT).show();
                                else if (!tryParseDouble(amountInput.getText().toString()))
                                    Toast.makeText(Event.this,"ERROR : amount is no number",Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(Event.this,"Cost Added!",Toast.LENGTH_SHORT).show();
                                    CostList.add(amountInput.getText().toString());
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog_amount.show();

                AlertDialog dialog_payer = new AlertDialog.Builder(Event.this)
                        .setTitle("Who paid?")
                        .setItems(people, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PayerList.add(GroupMembers.get(which));
                            }
                        })

                        .setPositiveButton("Add Cost", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Event.this,"Cost Added!",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog_payer.show();
            }
        });
        //Remove Payment
        lv_Cost.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(Event.this)
                        .setTitle("Delete this Payment?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    CostNameList.remove(position);
                                }
                                catch (Exception e){

                                }
                                try {
                                    CostList.remove(position);
                                }
                                catch (Exception e){

                                }
                                try {
                                    PayerList.remove(position);
                                }
                                catch (Exception e){

                                }
                                cost_adapter.setData(CostNameList);
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();
                return true;
            }
        });

        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPlanList.clear();
                payplan_adapter.setData(PayPlanList);
                if(CostNameList.isEmpty()){
                    return;
                }
                CalculatePayment();
                DecidePayPlan();

            }
        });
    }
    //METHODS
    boolean tryParseDouble(String value){
        try {
            Double.parseDouble(value);
            return  true;
        }
        catch (NumberFormatException e)
        {
            return  false;
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        SavePayments();
        SavePaymentPlan();
    }
    //Read & Save Payments
    private void SavePayments(){
        try{
            File file = new File(this.getFilesDir(), (_groupname + "_" + _eventname + "_paymentlist_file"));

            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fOut));

            for (int i = 0 ; i < CostNameList.size() ; i ++){
                writer.write(CostNameList.get(i));
                writer.newLine();
            }
            writer.close();
            fOut.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void ReadPayments(){
        File file = new File(this.getFilesDir(), (_groupname + "_" + _eventname + "_paymentlist_file"));

        if(!file.exists()){
            return;
        }
        try {
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
            String line = reader.readLine();
            while (line != null){
                CostNameList.add(line);
                line = reader.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Read & Save PaymentPlan
    private void SavePaymentPlan(){
        try{
            File file = new File(this.getFilesDir(), (_groupname + "_" + _eventname + "_paymentplan_file"));

            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fOut));

            for (int i = 0 ; i < PayPlanList.size() ; i ++){
                writer.write(PayPlanList.get(i));
                writer.newLine();
            }
            writer.close();
            fOut.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void ReadPaymentPlan(){
        File file = new File(this.getFilesDir(), (_groupname + "_" + _eventname + "_paymentplan_file"));

        if(!file.exists()){
            return;
        }
        try {
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
            String line = reader.readLine();
            while (line != null){
                PayPlanList.add(line);
                line = reader.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    //ADAPTER
    class CostAdapter extends BaseAdapter {
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
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) Event.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.payment_item, parent, false);
            }

            TextView textview = convertView.findViewById(R.id.itemCost);

            textview.setText(list.get(position));
            return convertView;
        }
    }
    class PaymentPlanAdapter extends BaseAdapter {
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
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) Event.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.paymentplan_item, parent, false);
            }

            TextView textview = convertView.findViewById(R.id.payplan_item);

            textview.setText(list.get(position));
            return convertView;
        }
    }

    //Calculate Payment plan
    private void CalculatePayment(){
        double totalAmount = 0, subtractOwnAmount = 0;
        for (String cost : CostList){
            totalAmount += (Double.parseDouble(cost)/GroupMembers.size());
        }
        for (String member : GroupMembers)
        {
            if (PayerList.contains(member));
            {
                int pos = PayerList.indexOf(member);
                subtractOwnAmount = Double.parseDouble(CostList.get(pos)) / GroupMembers.size();
            }
            AmountToPayList.add((totalAmount - subtractOwnAmount));
        }
    }
    private void DecidePayPlan(){
        String personWhoGetsMoney, personWhoOwesMoney, payPlanLine, restPerson = "";
        List<Double> restList = new ArrayList<Double>();
        for (String cost : CostList){
            int posCost = CostList.indexOf(cost);
            personWhoGetsMoney = PayerList.get(posCost);
            double amount = Double.parseDouble(cost);

            for (double money : AmountToPayList){
                int posMoney = AmountToPayList.indexOf(money);
                personWhoOwesMoney = GroupMembers.get(posMoney);

                if(personWhoGetsMoney == personWhoOwesMoney){
                    break;
                }

                if (!restList.isEmpty()){
                    double rest = restList.get(0);
                    payPlanLine = personWhoGetsMoney + " gets " + rest + "euros from " + restPerson;
                    PayPlanList.add(payPlanLine);
                    restList.remove(0);
                    amount -= rest;
                }

                if (amount - money >0){
                    payPlanLine = personWhoGetsMoney + " gets " + money + "euros from " + personWhoOwesMoney;
                    PayPlanList.add(payPlanLine);
                    amount -= money;
                }
                else if (amount - money == 0){
                    payPlanLine = personWhoGetsMoney + " gets " + money + "euros from " + personWhoOwesMoney;
                    PayPlanList.add(payPlanLine);
                    break;
                }
                else{
                    double rest = money - amount;
                    money -= rest;
                    restList.add(rest);
                    restPerson = personWhoOwesMoney;
                    payPlanLine = personWhoGetsMoney + " gets " + money + "euros from " + personWhoOwesMoney;
                    PayPlanList.add(payPlanLine);
                    break;
                }
            }
        }
    }
}
