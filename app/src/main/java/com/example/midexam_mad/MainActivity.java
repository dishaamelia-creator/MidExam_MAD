
package com.example.midexam_mad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etStudentId;
    Spinner spMajor;
    Button btnAdd, btnView;

    DBHelper dbHelper;

    Integer studentDatabaseId = null;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etStudentId = findViewById(R.id.etStudentId);
        spMajor = findViewById(R.id.spMajor);
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);

        dbHelper = new DBHelper(this);

        String[] majors = {
                "Informatics",
                "Information Systems"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                majors
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spMajor.setAdapter(adapter);

        // CHECK EDIT MODE
        Intent intent = getIntent();

        if (intent.hasExtra("student_id")) {

            isEditMode = true;

            studentDatabaseId = intent.getIntExtra("student_id", -1);

            String name = intent.getStringExtra("student_name");
            String studentId = intent.getStringExtra("student_number");
            String major = intent.getStringExtra("student_major");

            etName.setText(name);
            etStudentId.setText(studentId);

            int majorPosition = adapter.getPosition(major);

            if (majorPosition >= 0) {
                spMajor.setSelection(majorPosition);
            }

            btnAdd.setText("UPDATE STUDENT");
        }

        // ADD OR UPDATE STUDENT
        btnAdd.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String studentId = etStudentId.getText().toString().trim();
            String major = spMajor.getSelectedItem().toString();

            if (name.isEmpty() || studentId.isEmpty()) {

                Toast.makeText(
                        MainActivity.this,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (!studentId.matches("[0-9]+")) {

                Toast.makeText(
                        MainActivity.this,
                        "Student ID must contain numbers only",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            boolean result;

            if (isEditMode) {

                StudentModel studentModel = new StudentModel(
                        studentDatabaseId,
                        name,
                        studentId,
                        major
                );

                result = dbHelper.updateStudent(studentModel);

            } else {

                result = dbHelper.addStudent(
                        name,
                        studentId,
                        major
                );
            }

            if (result) {

                Toast.makeText(
                        MainActivity.this,
                        isEditMode
                                ? "Student updated successfully"
                                : "Student added successfully",
                        Toast.LENGTH_SHORT
                ).show();

                if (isEditMode) {
                    finish();
                } else {
                    etName.setText("");
                    etStudentId.setText("");
                    spMajor.setSelection(0);
                }

            } else {

                Toast.makeText(
                        MainActivity.this,
                        isEditMode
                                ? "Student ID already exists. Please use another ID."
                                : "Student ID already exists.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        // VIEW STUDENTS
        btnView.setOnClickListener(v -> {

            Intent viewIntent = new Intent(
                    MainActivity.this,
                    ViewStudent.class
            );

            startActivity(viewIntent);
        });
    }
}
