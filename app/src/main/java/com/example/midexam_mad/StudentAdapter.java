
package com.example.midexam_mad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    Context context;
    List<StudentModel> studentModels;
    DBHelper dbHelper;

    public StudentAdapter(Context context, List<StudentModel> studentModels) {
        this.context = context;
        this.studentModels = studentModels;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item_list, parent, false);

        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

        StudentModel studentModel = studentModels.get(position);

        holder.textName.setText(studentModel.getName());
        holder.textStudentId.setText(studentModel.getStudentId());
        holder.textMajor.setText(studentModel.getMajor());

        // EDIT STUDENT
        holder.btnEdit.setOnClickListener(v -> {

            Intent intent = new Intent(context, MainActivity.class);

            intent.putExtra("student_id", studentModel.getId());
            intent.putExtra("student_name", studentModel.getName());
            intent.putExtra("student_number", studentModel.getStudentId());
            intent.putExtra("student_major", studentModel.getMajor());

            context.startActivity(intent);
        });

        // DELETE STUDENT
        holder.btnDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete this student?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        boolean deleted = dbHelper.deleteStudent(studentModel.getId());

                        if (deleted) {
                            Toast.makeText(
                                    context,
                                    "Student deleted successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            studentModels.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, studentModels.size());

                        } else {
                            Toast.makeText(
                                    context,
                                    "Failed to delete student",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return studentModels.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textStudentId, textMajor;
        Button btnEdit, btnDelete;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.rowStudentName);
            textStudentId = itemView.findViewById(R.id.rowStudentId);
            textMajor = itemView.findViewById(R.id.rowMajor);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
