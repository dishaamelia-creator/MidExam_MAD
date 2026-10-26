
package com.example.midexam_mad;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewStudent extends AppCompatActivity {

    RecyclerView recyclerView;
    StudentAdapter adapter;
    DBHelper dbHelper;
    ArrayList<StudentModel> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        recyclerView = findViewById(R.id.recyclerView);

        dbHelper = new DBHelper(this);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadStudentData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dbHelper != null && recyclerView != null) {
            loadStudentData();
        }
    }

    private void loadStudentData() {

        studentList = dbHelper.getStudentList();

        adapter = new StudentAdapter(this, studentList);

        recyclerView.setAdapter(adapter);
    }
}
