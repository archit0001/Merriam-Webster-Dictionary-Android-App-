package com.example.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "FinalProject";
    public final static String TABLE_NAME = "DefinationWord";
    public final static String COL_WORD = "word";
    public final static String COL_DEF = "defination";
    public static final String COL_ID = "id";
    public final static int VERSION_NUMBER = 1;
    public final static String[] ALL_COLS = new String[]{COL_ID, COL_WORD, COL_DEF};
    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table query
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER primary key autoincrement, " + COL_WORD + " text, " + COL_DEF + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Delete old table
        db.execSQL("drop table if exists " + TABLE_NAME);
        //Create new table
        onCreate(db);
    }

    public long insertWord(String word, String defination) {

        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_WORD, word);//set word to word column
        cv.put(COL_DEF, defination);//set defination to defination column
        return db.insert(TABLE_NAME, null, cv);//return id

    }

    public void deleteWord(long id) {//Delete word query
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + " = '" + id + "';");
    }


    public List<SavedWord> getAll() {//Get data from database
        SQLiteDatabase db = getWritableDatabase();
        List<SavedWord> wordList = new ArrayList<>();

        //get all the results from the databse
        Cursor cursor = db.query(TABLE_NAME, ALL_COLS, null, null, null, null, null);

        //Loop for getting nxt items
        while (cursor.moveToNext()) {

            //Find id,word,defination in column
            final long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
            final String word = cursor.getString(cursor.getColumnIndex(COL_WORD));
            final String defination = cursor.getString(cursor.getColumnIndex(COL_DEF));

            //add new word object to list
            wordList.add(new SavedWord(id, word, defination));

        }
        cursor.close();
        return wordList;//Return list
    }

    public Cursor getData() {
        db = this.getReadableDatabase();

        Cursor c = db.rawQuery("select * from " + TABLE_NAME + ";", null);
        Log.d("Cursor Object", DatabaseUtils.dumpCursorToString(c));

        return c;
    }

}
