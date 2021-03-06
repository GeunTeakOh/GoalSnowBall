package com.taek_aaa.goalsnowball.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by taek_aaa on 2017. 1. 20..
 */

//db 이름 userInfo
public class UserDBManager extends SQLiteOpenHelper {



    public volatile static UserDBManager userDBManagerInstance;
    private static final String USERDATABASE_NAME = "userdb.db";
    private static final int USERDATABASE_VERSION = 1;

    public static synchronized UserDBManager getInstance(Context context) {
        if (userDBManagerInstance == null) {
            userDBManagerInstance = new UserDBManager(context.getApplicationContext());
        }
        return userDBManagerInstance;
    }

    public UserDBManager(Context context) {
        super(context, USERDATABASE_NAME, null, USERDATABASE_VERSION);

    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE userInfo (_id INTEGER PRIMARY KEY AUTOINCREMENT, grade TEXT, name TEXT, gold INTEGER, picturePath TEXT, rotationIter INTEGER, hasNotification INTEGER, hasSound INTEGER, notiTime INTEGER, notiMinute INTEGER);");
        db.execSQL("INSERT INTO userInfo VALUES(NULL, '" + "UnRank" + "', '" + "Insert_Name" + "', " + 10 + ", '" + "null" + "', " + 0 + ", " + 1 + ", "+ 1 + "," + 0 + "," + 0 + " );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists userInfo;");
        onCreate(db);
    }

    public void addRotationIter(){
        int a = getRotationIter();
        a = a+1;
        if(a>3){
            a=a%4;
        }
        setRotationIter(a);
    }
    public void setRotationIter(int a) {
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET rotationIter="+a+";";
        db.execSQL(sql);
        db.close();
    }

    public void setGrade(String str) {
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET grade='"+str+"';";
        db.execSQL(sql);
        db.close();
    }
    public void setName(String str){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET name='"+str+"';";
        db.execSQL(sql);
        db.close();
    }
    public void setGold(int gd){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET gold="+gd;
        db.execSQL(sql);
        db.close();
    }
    public void setPicturePath(String str){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET picturePath='"+str+"';";
        db.execSQL(sql);
        db.close();
    }

    public void setIsNoti(int a){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET hasNotification="+a+";";
        db.execSQL(sql);
        db.close();
    }


    public void setIsSound(int a){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET hasSound="+a;
        db.execSQL(sql);
        db.close();
    }

    public void setNotiTime(int a){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET notiTime="+a;
        db.execSQL(sql);
        db.close();
    }
    public void setNotiMinute(int a){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET notiMinute="+a;
        db.execSQL(sql);
        db.close();
    }


    public String getGrade() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        String grade="";
        while (cursor.moveToNext()) {
            grade = cursor.getString(cursor.getColumnIndex("grade"));
        }
        cursor.close();
        db.close();
        return grade;
    }
    public String getName() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        String name="";
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
        }
        cursor.close();
        db.close();
        return name;
    }
    public int getGold() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        int gold=0;
        while (cursor.moveToNext()) {
            gold = cursor.getInt(cursor.getColumnIndex("gold"));
        }
        cursor.close();
        db.close();
        return gold;
    }
    public String getPicturePath() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        String Path="";
        while (cursor.moveToNext()) {
            Path = cursor.getString(cursor.getColumnIndex("picturePath"));
        }
        cursor.close();
        db.close();
        return Path;
    }
    public int getRotationIter() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        int iter=0;
        while (cursor.moveToNext()) {
            iter = cursor.getInt(cursor.getColumnIndex("rotationIter"));
        }
        cursor.close();
        db.close();
        return iter;
    }

    public int getIsNoti() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        int a=0;
        while (cursor.moveToNext()) {
            a = cursor.getInt(cursor.getColumnIndex("hasNotification"));
        }
        cursor.close();
        db.close();
        return a;
    }
    public int getIsSound() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        int a=0;
        while (cursor.moveToNext()) {
            a = cursor.getInt(cursor.getColumnIndex("hasSound"));
        }
        cursor.close();
        db.close();
        return a;
    }

    public int getNotiTime(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        int a=0;
        while (cursor.moveToNext()) {
            a = cursor.getInt(cursor.getColumnIndex("notiTime"));
        }
        cursor.close();
        db.close();
        return a;
    }
    public int getNotiMinute(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        int a=0;
        while (cursor.moveToNext()) {
            a = cursor.getInt(cursor.getColumnIndex("notiMinute"));
        }
        cursor.close();
        db.close();
        return a;
    }


}
