package com.example.demo;

public class SavedWord {
    public String word;
    public String defination;

    public String[] definationArray;
    public long id;

//    public SavedWord(long id, String word, String[] definationArray) {
//        this.id = id;
//        this.word = word;
//        this.definationArray = definationArray;
//    }

    /**
     * Constructor:
     */
    public SavedWord(long id, String word, String defination) {
        this.id = id;
        this.word = word;
        this.defination = defination;
    }


    public SavedWord(String word, String defination) {
        this.word = word;
        this.defination = defination;
    }

    public String getDefination() {
        return defination;
    }

    public void setDefination(String defination) {
        this.defination = defination;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}