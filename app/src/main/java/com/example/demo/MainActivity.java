package com.example.demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

//import androidx.annotation.RequiresApi;

public class MainActivity extends AppCompatActivity {

    RelativeLayout toolLayout;
    SharedPreferences sp;
    String defination;
    String word = null;
    private Toolbar myToolbar;
    private EditText eText;
    private Button sButton;
    private ListView lView;
    private ListView listView;
    private TextView tView;
    private ArrayList<SavedWord> wdList = new ArrayList<>();
    private ArrayList<String> definationList = new ArrayList<String>();
    private ListAdapter listAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseHelper databaseHelper;
    private SavedWord savedWord;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.about) {
            Intent intent = new Intent(this, Info.class);
            startActivity(intent);
            makeText(this, "Info", LENGTH_LONG).show();
        }

        if (itemId == R.id.addToList) {
            if (eText.getText().toString().equals(word)) {
                definationConcate();//concate defination to save all definations in one id
                addData(word, defination);//add word, defination to databas
                makeText(this, "Definations added in list", LENGTH_SHORT).show();
            } else {
                makeText(this, "Search a word", LENGTH_SHORT).show();
            }
        }

        if (itemId == R.id.saved) {
            makeText(this, "saved definationList", LENGTH_SHORT).show();

            Intent intent = new Intent(this, SavedDefination.class);
            startActivity(intent);
        }

        if (itemId == R.id.exit) {
            showSnackbar(toolLayout);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Get the fields from the screen:
        toolLayout = findViewById(R.id.main);
        eText = (EditText) findViewById(R.id.searchWord);
        sButton = (Button) findViewById(R.id.search);
        lView = (ListView) findViewById(R.id.setDefination);
        listView = (ListView) findViewById(R.id.savedWordList);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, definationList);

        databaseHelper = new DatabaseHelper(this);


        sButton.setOnClickListener(c -> {

            definationList.clear();//Clear defination list
            arrayAdapter.notifyDataSetChanged();//Update Adapter

            SharedPreferences.Editor editor = sp.edit();//
            word = eText.getText().toString();
            editor.putString("SavedWord", word);
            editor.commit();

            String query = null;
            try {
                query = URLEncoder.encode(word, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            DictionaryQuery dictionaryQuery = new DictionaryQuery();
            dictionaryQuery.execute("https://www.dictionaryapi.com/api/v1/references/sd3/xml/" + query + "?key=4556541c-b8ed-4674-9620-b6cba447184f");

        });

        sp = getSharedPreferences("lastWord", Context.MODE_PRIVATE);
        String savedString = sp.getString("SavedWord", "");
        eText.setText(savedString);

    }

    public void showSnackbar(View view) {
//Showing snackbar
        final Snackbar sb = Snackbar.make(lView, "Want to exit?", Snackbar.LENGTH_LONG);
        sb.setAction("Exit", e -> finish());
        sb.show();
    }

    public void addData(String word, String defination) {//add data to database methode

        SavedWord savedWord = new SavedWord(word, defination);
        long id = databaseHelper.insertWord(word, defination);
        savedWord.setId(id);

        if (!word.equals("")) {
            wdList.add(savedWord);
        } else {
            makeText(this, "Please enter a word", LENGTH_SHORT);
        }
    }

    public void definationConcate() {
        defination = definationList.get(0);
        for (int i = 1; i < definationList.size(); i++) {
            defination = defination.concat(definationList.get(i));
        }
    }

    private class DictionaryQuery extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPostExecute(String s) {
            lView.setAdapter(arrayAdapter);
        }


        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            String urlString = strings[0];
            try {
                URL wordURL = new URL(urlString);
                urlConnection = (HttpURLConnection) wordURL.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(urlConnection.getInputStream(), "UTF-8");


                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = xpp.getName();//get tagname


                        if (tagName.equals("dt")) {//get dt tagName
                            xpp.next();//looking for tag string
                            definationList.add(xpp.getText());//add defination to the list
                        }
                    }
                    xpp.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Finished";
        }


    }

}