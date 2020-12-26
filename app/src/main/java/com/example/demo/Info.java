package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Info extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView textView = (TextView) findViewById(R.id.infoText);

        String info = "Version junior\n" +
                "Just simple trailer\n\n " +
                "By Archit Anghan";
        textView.setText(info);

    }
}
