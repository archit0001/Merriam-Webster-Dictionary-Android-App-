package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DefinationText extends Activity {


    DatabaseHelper databaseHelper;
    SavedDefination savedDefination;
    private TextView defView;
    private FloatingActionButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);

        defView = (TextView) findViewById(R.id.definationText);
        defView.setMaxLines(Integer.MAX_VALUE);//Set max.line to textView
        deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);//Initilize Floating Button

        Intent intent = getIntent();//Get intent

        Bundle bundle = intent.getExtras();
        long itemID = bundle.getLong("id");//Get value from Key id
        int position = bundle.getInt("position");//Get value from Key position
        String defination = bundle.getString("def");//Get value from Key def

        int index = 1;

        String[] str = defination.split(":");
        StringBuffer sb = new StringBuffer(index + "." + str[1]);

        String newString;
        for (int i = 2; i < str.length; i++) {

//        newString=str[i];
//        sb.append(newString+"\n\n"+index+".");
            index++;
            newString = "\n\n" + index + "." + str[i];
            sb.append(newString);
        }


        defView.setText(sb.toString());


        deleteButton.setOnClickListener(d -> {

            Intent backToActivity = new Intent();
            backToActivity.putExtra("id", bundle.getLong("id"));//set value for Key id
            setResult(Activity.RESULT_OK, backToActivity);//send data back to SavedDefination in onActivityResult()
            finish();//Go to back Activity
        });
    }
}