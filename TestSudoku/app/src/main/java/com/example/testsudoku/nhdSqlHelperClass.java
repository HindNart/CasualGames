package com.example.testsudoku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class nhdSqlHelperClass extends SQLiteOpenHelper {

    // Database and table details
    private static final String DATABASE_NAME = "game.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "game_data";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_POINT = "point";
    public static final String COLUMN_CHECK1 = "check1";
    public static final String COLUMN_CHECK2 = "check2";
    public static final String COLUMN_CHECK3 = "check3";
    public static final String COLUMN_CHECK4 = "check4";
    public static final String COLUMN_CHECK5 = "check5";
    public static final String COLUMN_CHECK6 = "check6";
    public static final String COLUMN_CHECK7 = "check7";

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SCORE + " INTEGER, " +
            COLUMN_POINT + " INTEGER, " +
            COLUMN_CHECK1 + " INTEGER DEFAULT 0, " +
            COLUMN_CHECK2 + " INTEGER DEFAULT 0, " +
            COLUMN_CHECK3 + " INTEGER DEFAULT 0, " +
            COLUMN_CHECK4 + " INTEGER DEFAULT 0, " +
            COLUMN_CHECK5 + " INTEGER DEFAULT 0, " +
            COLUMN_CHECK6 + " INTEGER DEFAULT 0, " +
            COLUMN_CHECK7 + " INTEGER DEFAULT 0);";

    public nhdSqlHelperClass(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
        }
    }

    public void updateChecks(int check) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            switch (check) {
                case 1:
                    contentValues.put("check1", 1);
                    break;
                case 2:
                    contentValues.put("check2", 1);
                    break;
                case 3:
                    contentValues.put("check3", 1);
                    break;
                case 4:
                    contentValues.put("check4", 1);
                    break;
                case 5:
                    contentValues.put("check5", 1);
                    break;
                case 6:
                    contentValues.put("check6", 1);
                    break;
                case 7:
                    contentValues.put("check7", 1);
                    break;
                default:
                    break;
            }
            Log.d("sql", "ContentValues for update: " + contentValues);

            // Update the record with id = 1
            int rowsAffected = db.update(TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(1)});
            Log.d("DatabaseUpdate", "Rows affected: " + rowsAffected);

            // If no rows were affected, insert a new record with id = 1
            if (rowsAffected == 0) {
                Log.d("DatabaseUpdate", "No record found with id = 1. Inserting new record.");
                contentValues.put(COLUMN_ID, 1);
                db.insert(TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DatabaseError", "Error updating database", e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Integer> getSelectedChecks() {
        List<Integer> selectedChecks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT check1, check2, check3, check4, check5, check6, check7 FROM " + TABLE_NAME + " WHERE id = ?", new String[]{String.valueOf(1)});
            if (cursor.moveToFirst()) {
                for (int i = 0; i < 7; i++) {
                    if (cursor.getInt(i) == 1) {
                        selectedChecks.add(i + 1);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error fetching selected checks", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        Log.d("sql", "Selected checks: " + selectedChecks);
        return selectedChecks;
    }

    public void updateScore( int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SCORE, score);

            int rowsAffected = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(1)});
            Log.d("DatabaseUpdate", "Rows affected for score: " + rowsAffected);

            if (rowsAffected == 0) {
                Log.d("DatabaseUpdate", "No record found with id = " + 1 + ". Inserting new record.");
                contentValues.put(COLUMN_ID, 1);
                db.insert(TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DatabaseError", "Error updating score", e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updatePoint(int point) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_POINT, point);

            int rowsAffected = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(1)});
            Log.d("DatabaseUpdate", "Rows affected for point: " + rowsAffected);

            if (rowsAffected == 0) {
                Log.d("DatabaseUpdate", "No record found with id = " + 1 + ". Inserting new record.");
                contentValues.put(COLUMN_ID, 1);
                db.insert(TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DatabaseError", "Error updating point", e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    public int getScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int score = -1;

        try {
            cursor = db.query(TABLE_NAME, new String[]{COLUMN_SCORE}, COLUMN_ID + " = ?", new String[]{String.valueOf(1)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int scoreIndex = cursor.getColumnIndex(COLUMN_SCORE);
                if (scoreIndex != -1) {
                    score = cursor.getInt(scoreIndex);
                } else {
                    Log.e("DatabaseError", "Column not found: " + COLUMN_SCORE);
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error fetching score", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        Log.d("sql", "Retrieved score: " + score);
        return score;
    }


    public int getPoint() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int point = -1;

        try {
            cursor = db.query(TABLE_NAME, new String[]{COLUMN_POINT}, COLUMN_ID + " = ?", new String[]{String.valueOf(1)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int pointIndex = cursor.getColumnIndex(COLUMN_POINT);
                if (pointIndex != -1) {
                    point = cursor.getInt(pointIndex);
                } else {
                    Log.e("DatabaseError", "Column not found: " + COLUMN_POINT);
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error fetching point", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        Log.d("sql", "Retrieved point: " + point);
        return point;
    }


}
