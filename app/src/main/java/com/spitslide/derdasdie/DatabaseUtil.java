package com.spitslide.derdasdie;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    private SQLiteDatabase database;
    private static final String SCORES_TABLE = "ScoresTable";
    private static final String NOUNS_TABLE = "NounsTable";


    public DatabaseUtil(Context context) {
        database = new DatabaseHelper(context).getWritableDatabase();
    }


    public void addScore(float score) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("score", score);
        database.insert(SCORES_TABLE, null, contentValues);
    }

    public List<Float> getAllScores() {
        String[] columns = new String[]{"score"};
        Cursor cursor =  database.query(SCORES_TABLE, columns, null, null, null, null, null);
        if (cursor == null) {
            return null;
        }

        List<Float> scores = new ArrayList<>();
        while (cursor.moveToNext()) {
            scores.add(cursor.getFloat(0));
        }
        cursor.close();
        return scores;
    }

    public float getLastScore() {
        String[] columns = new String[]{"score"};
        String orderBy = "_id DESC";
        String limit = "1";
        Cursor cursor = database.query(SCORES_TABLE, columns, null, null, null, null, orderBy, limit);
        float lastScore;
        if (cursor != null && cursor.moveToFirst()) {
            lastScore = cursor.getFloat(0);
            cursor.close();
        } else {
            lastScore = 0;
        }
        return lastScore;
    }


    public void addAllNouns(List<Noun> nouns) {
        database.beginTransaction();
        database.delete(NOUNS_TABLE, null, null);
        ContentValues contentValues = new ContentValues();
        for (Noun noun : nouns) {
            contentValues.put("noun", noun.getNoun());
            contentValues.put("gender", noun.getGender());
            contentValues.put("times_answered", noun.getTimesAnswered());
            database.insert(NOUNS_TABLE, null, contentValues);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public List<Noun> getAllNouns() {
        List<Noun> nouns = new ArrayList<>();
        String[] columns = new String[]{"noun", "gender", "times_answered"};
        Cursor cursor = database.query(NOUNS_TABLE, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Noun noun = new Noun(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
            nouns.add(noun);
        }
        cursor.close();
        return nouns;
    }

    public long getNounsCount() {
        return DatabaseUtils.queryNumEntries(database, NOUNS_TABLE);
    }



    public Noun getFirstNoun(){
        String[] columns = new String[]{"noun", "gender", "times_answered"};
        String limit = "1";
        Cursor cursor = database.query(NOUNS_TABLE, columns, null, null, null, null, null, limit);
        Noun noun;
        if (cursor != null) {
            cursor.moveToFirst();
            noun = new Noun(cursor.getString(0), cursor.getString(1), cursor.getInt(0));
            cursor.close();
        } else {
            // TODO - if no more words
            noun = new Noun("", "", 0);
        }
        return noun;
    }




    class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "Database";
        private static final int DATABASE_VERSION = 1;
        private static final String CREATE_SCORES_TABLE = "CREATE TABLE IF NOT EXISTS " + SCORES_TABLE + " (_id INTEGER PRIMARY KEY, score REAL);";
        private static final String CREATE_NOUNS_TABLE = "CREATE TABLE IF NOT EXISTS " + NOUNS_TABLE + " (_id INTEGER PRIMARY KEY, noun TEXT, gender TEXT, times_answered INTEGER);";

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_SCORES_TABLE);
            sqLiteDatabase.execSQL(CREATE_NOUNS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }
    }
}
