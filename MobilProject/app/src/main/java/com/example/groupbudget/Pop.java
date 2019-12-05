package com.example.groupbudget;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

public class Pop extends Activity {
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.6));

        WindowManager.LayoutParams parans = getWindow().getAttributes();
        parans.gravity = Gravity.CENTER;
        parans.x = 0;
        parans.y = -20;

        getWindow().setAttributes(parans);
    }
}
