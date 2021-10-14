package com.adrian.practica4.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.adrian.practica4.model.Articulo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "test_practica_4.db";
    public static final int DB_VERSION = 1;

    protected static final int FIELD_TYPE_BLOB = 4;
    protected static final int FIELD_TYPE_FLOAT = 2;
    protected static final int FIELD_TYPE_INTEGER = 1;
    protected static final int FIELD_TYPE_NULL = 0;
    protected static final int FIELD_TYPE_STRING = 3;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MessageFormat.format("CREATE TABLE {0}({1})", Articulo.NAME,Articulo.getColumns()));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MessageFormat.format("DROP TABLE ",Articulo.NAME));
        onCreate(db);
    }
    private SQLiteDatabase Open(){
        return getWritableDatabase();
    }
    private void Close(){
        close();
    }

    public long insert(String name, ContentValues content){
        long rows = Open().insert(name,null, content);
        Close();
        return rows;
    }

    public int update(String name, ContentValues content, String where){
        int rows = Open().update(name,content,where, null);
        Close();
        return rows;
    }

    public int remove(String name, String where){
        int rows = Open().delete(name, where, null);
        Close();
        return rows;
    }


    public JSONArray select(String sql, String[] args){
        Cursor cursor = Open().rawQuery(sql,args);
        if(cursor.getCount() < 1)
            return null;
        if (!cursor.moveToFirst())
            return null;
        JSONArray array = new JSONArray();
        do{
            JSONObject object = new JSONObject();
            for (int a=0; a < cursor.getColumnNames().length; a++) {
                try {
                    object.put(cursor.getColumnName(a),
                            cursor.getType(a) == FIELD_TYPE_INTEGER ? cursor.getInt(a) :
                                    cursor.getType(a) == FIELD_TYPE_FLOAT ? cursor.getFloat(a) :
                                            cursor.getType(a) == FIELD_TYPE_STRING || cursor.getType(a) == FIELD_TYPE_NULL ? cursor.getString(a):
                                                    cursor.getBlob(a) );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            array.put(object);
        }while (cursor.moveToNext());
        return array;
    }
}
