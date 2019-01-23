package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.model.Video;

import java.util.ArrayList;

public class MobioticsDb extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "Database";

    // Contacts table name
    private static final String RECORD_TABLE = "Videos";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String THUMB = "thumb";
    private static final String URL = "url";
    private static final String START_TIME = "startTime";

    public MobioticsDb(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + RECORD_TABLE + "("
                + KEY_ID + " Text Primary Key," + TITLE + " TEXT," + DESCRIPTION + " TEXT," + THUMB + " TEXT,"
                + URL + " TEXT," + START_TIME + " TEXT"
                + ")";

        db.execSQL(CREATE_NOTIFICATION_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + RECORD_TABLE);
        // Create tables again
        onCreate(db);
    }

    // Adding new
    public void addVideo(Video VideoArrayList, String starttime) {

        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Id",VideoArrayList.getid());
            values.put(TITLE, VideoArrayList.getTitle());
            values.put(DESCRIPTION, VideoArrayList.getDescription());
            values.put(THUMB, VideoArrayList.getUrlToImage());
            values.put(URL, VideoArrayList.getUrl());
            values.put(START_TIME, starttime);
            db.insert(RECORD_TABLE, null, values);
        db.close(); // Closing database connection
    }

    // Getting All
    public ArrayList<Video> getAllVideo() {
        ArrayList<Video> VideoArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + RECORD_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        while(cursor.moveToNext()){
                Video Video = new Video(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
                VideoArrayList.add(Video);
            }
        cursor.close();
        return VideoArrayList;
    }

    public void updateRecord(String Id, String start) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("Here","Hetre");
        String selectQuery = "UPDATE " + RECORD_TABLE+ " SET startTime = "+start+" where id="+Id;
        db.execSQL(selectQuery);
        Log.d("after Insert",  "Done");
        db.close();
    }


    public String getRecord(String id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(RECORD_TABLE, new String[]{KEY_ID, TITLE, DESCRIPTION, THUMB, URL, START_TIME}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        String time="26";
        if (cursor != null)
        while(cursor.moveToNext()){
        time=cursor.getString(5);}
        cursor.close();
        db.close();
        return time;
    }

    public Video getNextRecord(String id) {

        ArrayList<Video> arraylist=getAllVideo();
        Log.e("Error","id1   "+id);
        for(int i=0;i<arraylist.size();i++)
        {
            Video v=arraylist.get(i);
            Log.e("error1","id2"+v.getid());
            if(v.getid().equals(id) && i!=arraylist.size()-1)
                return arraylist.get(i+1);
            else if(v.getid().equals(id) && i==arraylist.size()-1)
                return  arraylist.get(0);
        }
        return arraylist.get(0);
    }


    public int getRecordCount() {
        String countQuery = "SELECT  * FROM " + RECORD_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}