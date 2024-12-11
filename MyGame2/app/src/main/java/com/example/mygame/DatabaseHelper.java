package com.example.mygame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "games.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "games";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IMAGE_NAME = "image_name";
    private static final String COLUMN_QUESTIONS = "questions";
    private static final String COLUMN_ANSWER = "answer";
    private static final String COLUMN_HINT = "hint";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE_NAME + " TEXT, " +
                COLUMN_QUESTIONS + " TEXT, " +
                COLUMN_ANSWER + " TEXT, " +
                COLUMN_HINT + " TEXT)";
        db.execSQL(createTable);

        // Thêm dữ liệu ban đầu vào bảng games
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to get a random question
    public Cursor getRandomQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY RANDOM() LIMIT 1";
        return db.rawQuery(query, null);
    }

    // Phương thức để thêm dữ liệu ban đầu vào bảng games
    private void insertInitialData(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_IMAGE_NAME, "image1");
        contentValues.put(COLUMN_QUESTIONS, "Đây là câu ca dao tục ngữ gì?");
        contentValues.put(COLUMN_ANSWER, "Cốc mò cò xơi");
        contentValues.put(COLUMN_HINT, "Từ gợi ý: Cốc..." );
        db.insert(TABLE_NAME, null, contentValues);

        contentValues.put(COLUMN_IMAGE_NAME, "image2");
        contentValues.put(COLUMN_QUESTIONS, "Đây là câu ca dao tục ngữ gì?");
        contentValues.put(COLUMN_ANSWER, "Đủ lông đủ cánh");
        contentValues.put(COLUMN_HINT, "Từ gợi ý: ...Cánh");
        db.insert(TABLE_NAME, null, contentValues);

        contentValues.put(COLUMN_IMAGE_NAME, "image3");
        contentValues.put(COLUMN_QUESTIONS, "Đây là câu ca dao tục ngữ gì?");
        contentValues.put(COLUMN_ANSWER, "Tiền trảm hậu tấu");
        contentValues.put(COLUMN_HINT, "Từ gợi ý: ...hậu...");
        db.insert(TABLE_NAME, null, contentValues);

        contentValues.put(COLUMN_IMAGE_NAME, "image4");
        contentValues.put(COLUMN_QUESTIONS, "Đây là câu ca dao tục ngữ gì?");
        contentValues.put(COLUMN_ANSWER, "Thọc gậy bánh xe");
        contentValues.put(COLUMN_HINT, "Từ gợi ý: Thọc...");
        db.insert(TABLE_NAME, null, contentValues);

        contentValues.put(COLUMN_IMAGE_NAME, "image5");
        contentValues.put(COLUMN_QUESTIONS, "Đây là câu ca dao tục ngữ gì?");
        contentValues.put(COLUMN_ANSWER, "Trời nóng chóng khát, trời mát chóng đói");
        contentValues.put(COLUMN_HINT, "Từ gợi ý: Trời...,trời...");
        db.insert(TABLE_NAME, null, contentValues);
        // Bạn có thể thêm nhiều dữ liệu ban đầu khác tại đây
    }

    public boolean addGame(String imageName, String questions, String answer, String hint) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IMAGE_NAME, imageName);
        contentValues.put(COLUMN_QUESTIONS, questions);
        contentValues.put(COLUMN_ANSWER, answer);
        contentValues.put(COLUMN_HINT, hint);

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        return result != -1; // Nếu thêm thành công, kết quả sẽ khác -1
    }

}
