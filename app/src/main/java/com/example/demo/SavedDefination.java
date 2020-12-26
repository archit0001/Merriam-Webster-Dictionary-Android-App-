package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SavedDefination extends Activity {

    public static final int DEFINATION_TEXT = 21;
    SavedWord savedWord;
    private DatabaseHelper databaseHelper;
    private ListView listView;
    private List<SavedWord> allList = new ArrayList<>();
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_defination);

        listView = findViewById(R.id.savedWordList);
        databaseHelper = new DatabaseHelper(this);//Intialize Database

        allList = databaseHelper.getAll();//Get all data from Database

        listAdapter = new ListAdapter(allList, this);//Set list to Adapter
        listView.setAdapter(listAdapter);//Set Adapter

        listView.setOnItemClickListener((parent, view, position, id) -> {

            savedWord = (SavedWord) listAdapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putLong("id", savedWord.getId());//Data send to the DefiantionText class
            bundle.putInt("position", position);//Data send to the DefiantionText class
            bundle.putString("def", savedWord.getDefination());//Data send to the DefiantionText class

            Intent intent = new Intent(this, DefinationText.class);//Go to next DefiantionText class
            intent.putExtras(bundle);//Send bundle to DefiantionText class
            startActivityForResult(intent, DEFINATION_TEXT);//Start intent

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == DEFINATION_TEXT) {//get requestCode from DefiantionText class
            if (resultCode == RESULT_OK) {
                long id = data.getLongExtra("id", 0);//get value for key id
                int position = data.getIntExtra("position", 0);//get value for key position
                deleteWordId(id, position);//Delete word
            }
        }

    }

    public void deleteWordId(long id, int position) {
        databaseHelper.deleteWord(id);//Delete id from Database
        allList.remove(position);//Remove from list
        listAdapter.notifyDataSetChanged();//Update Adapter view

    }


}
