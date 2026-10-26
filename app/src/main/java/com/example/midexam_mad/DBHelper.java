
package com.example.midexam_mad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "student_db";
    private static final String TB_NAME = "student";

    private static final String Col_ID = "id";
    private static final String Col_Name = "name";
    private static final String Col_StudentID = "student_id";
    private static final String Col_Major = "major";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TB_NAME + " (" +
                    Col_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Col_Name + " TEXT NOT NULL, " +
                    Col_StudentID + " TEXT NOT NULL UNIQUE, " +
                    Col_Major + " TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    // ADD STUDENT
    public boolean addStudent(String name, String studentId, String major) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(Col_Name, name);
        contentValues.put(Col_StudentID, studentId);
        contentValues.put(Col_Major, major);

        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert(TB_NAME, null, contentValues);

        return result != -1;
    }

    // VIEW STUDENT
    public ArrayList<StudentModel> getStudentList() {

        ArrayList<StudentModel> studentModels = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TB_NAME,
                null
        );

        if (cursor.moveToFirst()) {

            do {
                Integer id = cursor.getInt(0);
                String name = cursor.getString(1);
                String studentId = cursor.getString(2);
                String major = cursor.getString(3);

                StudentModel studentModel =
                        new StudentModel(id, name, studentId, major);

                studentModels.add(studentModel);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return studentModels;
    }

    // EDIT STUDENT

    // EDIT STUDENT
    public boolean updateStudent(StudentModel studentModel) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(Col_Name, studentModel.getName());
        contentValues.put(Col_StudentID, studentModel.getStudentId());
        contentValues.put(Col_Major, studentModel.getMajor());

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            int result = db.update(
                    TB_NAME,
                    contentValues,
                    Col_ID + " = ?",
                    new String[]{String.valueOf(studentModel.getId())}
            );

            return result > 0;

        } catch (android.database.sqlite.SQLiteConstraintException e) {
            return false;
        }
    }


    // DELETE STUDENT
    public boolean deleteStudent(Integer id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TB_NAME,
                Col_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }
}
