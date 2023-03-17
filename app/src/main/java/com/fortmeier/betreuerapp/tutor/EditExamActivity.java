package com.fortmeier.betreuerapp.tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fortmeier.betreuerapp.MainActivity;
import com.fortmeier.betreuerapp.R;
import com.fortmeier.betreuerapp.model.Exam;
import com.fortmeier.betreuerapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class EditExamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView tvStudentFullName;
    private Spinner spinnerStatus;
    private Spinner spinnerBillStatus;
    private EditText etSecondAssessorName;
    private EditText etSecondAssessorFirstName;
    private Button btnSubmit;
    private Button btnCancel;
    private Button btnDelete;
    private HashMap<String, User> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exam);
        etSecondAssessorName = findViewById(R.id.et_edit_exam_second_assessor_name);
        etSecondAssessorFirstName = findViewById(R.id.et_edit_exam_second_assessor_first_name);
        btnSubmit = findViewById(R.id.btn_edit_exam_submit);
        btnCancel = findViewById(R.id.btn_edit_exam_cancel);
        btnDelete = findViewById(R.id.btn_edit_exam_delete);
        tvStudentFullName = findViewById(R.id.tv_create_exam_student_full_name);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Exam exam = (Exam) getIntent().getSerializableExtra("Exam");
        userData = (HashMap<String, User>) getIntent().getSerializableExtra("map");
        spinnerStatus = (Spinner) findViewById(R.id.spinner_status);
        spinnerStatus.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);

        spinnerBillStatus = (Spinner) findViewById(R.id.spinner_bill_status);
        spinnerBillStatus.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterBillStatus = ArrayAdapter.createFromResource(this,
                R.array.bill_status, android.R.layout.simple_spinner_item);
        adapterBillStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBillStatus.setAdapter(adapterBillStatus);

        setSpinnerSelection(exam);

        tvStudentFullName.setText(exam.getStudentName());
        etSecondAssessorName.setText(exam.getSecondAssessorName());
        etSecondAssessorFirstName.setText(exam.getSecondAssessorFirstName());


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> examUpdateMap = new HashMap<>();
                examUpdateMap.put("secondAssessorFirstName", etSecondAssessorFirstName.getText().toString());
                examUpdateMap.put("secondAssessorName", etSecondAssessorName.getText().toString());
                examUpdateMap.put("status", spinnerStatus.getSelectedItem().toString());
                examUpdateMap.put("billStatus", spinnerBillStatus.getSelectedItem().toString());
                db.collection("Exams").document(exam.getId()).update(examUpdateMap);

                Toast.makeText(EditExamActivity.this, "Änderungen wurden übernommen.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditExamActivity.this, TutorActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditExamActivity.this, TutorActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Exams").document(exam.getId()).delete();
                Toast.makeText(EditExamActivity.this, "Item wurde gelöscht!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditExamActivity.this, TutorActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });


    }

    private void setSpinnerSelection(Exam exam) {
        switch (exam.getStatus()) {
            case "In Abstimmung":
                spinnerStatus.setSelection(0);
                break;
            case "Angemeldet":
                spinnerStatus.setSelection(1);
                break;
            case "Abgegeben":
                spinnerStatus.setSelection(2);
                break;
            case "Kolloquium abgehalten":
                spinnerStatus.setSelection(3);
                break;
        }
        switch (exam.getBillStatus()) {
            case "Rechnung noch nicht erstellt":
                spinnerBillStatus.setSelection(0);
                break;
            case "Rechnung wurde gestellt":
                spinnerBillStatus.setSelection(1);
                break;
            case "Rechnung beglichen":
                spinnerBillStatus.setSelection(2);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}