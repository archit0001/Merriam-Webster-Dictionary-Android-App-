package com.example.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    private List<SavedWord> wordList;
    private Context context;
    private LayoutInflater inflater;

    public ListAdapter(List<SavedWord> wordList, Context context) {
        this.wordList = wordList;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public Object getItem(int position) {
        return wordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;


        if (wordList.get(position).getId() >= 0) {
            view = inflater.inflate(R.layout.set_word, null);//Inflate view

        }


        TextView definationWord = (TextView) view.findViewById(R.id.wordText);
        definationWord.setText(wordList.get(position).word);//set word to list

        return view;
    }


}


