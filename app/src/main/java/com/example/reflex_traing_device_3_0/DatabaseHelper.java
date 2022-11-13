package com.example.reflex_traing_device_3_0;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "patient.db";
    public static final String TABLE_NAME = "patient_table";
    public static final String COL_1 = "ACTIVITY";
    public static final String COL_2 = "REACTION_TIME";
    public static final String COL_3 = "SCORE";
    public static final String COL_4 = "ACCURACY";
    public static final String COL_5 = "DURATION";
    public static final String COL_6 = "MAXIMUM_FORCE";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"(ACTIVITY INTEGER,REACTION_TIME INTEGER,SCORE INTEGER,ACCURACY FLOAT,DURATION INTEGER,MAXIMUM_FORCE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(Integer activity, Integer reaction_time, Integer score, Float accuracy, Integer duration, Integer maximum_force) {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,activity);
        contentValues.put(COL_2,reaction_time);
        contentValues.put(COL_3,score);
        contentValues.put(COL_4,accuracy);
        contentValues.put(COL_5,duration);
        contentValues.put(COL_6,maximum_force);
        Long res=db.insert(TABLE_NAME, null,contentValues);
        if (res==-1)
        {return false;}
        else
        {return true;}
    }

    public void insertData(String toString) {
    }

    public void insertData(String toString, String toString1, String toString2, String toString3, String toString4, String toString5) {
    }
}
