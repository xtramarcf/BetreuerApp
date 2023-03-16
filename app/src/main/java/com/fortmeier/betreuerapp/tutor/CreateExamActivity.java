package com.fortmeier.betreuerapp.tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fortmeier.betreuerapp.MainActivity;
import com.fortmeier.betreuerapp.R;
import com.fortmeier.betreuerapp.model.Exam;
import com.fortmeier.betreuerapp.model.Topic;
import com.fortmeier.betreuerapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateExamActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private EditText etSubject;
    private EditText etAreaOfSubject;
    private EditText etStudentName;
    private EditText etStudentFirstName;
    private EditText etStudentEmail;
    private EditText etSecondAssessorName;
    private EditText etSecondAssessorFirstName;
    private Button btnSubmit;
    private Button btnCancel;
    private TextView tvFailure;
    private String tutorFullName;
    private String tutorEMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam);
        initializeViewComponents();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();
        tutorEMail = user.getEmail();

        DocumentReference docRef = db.collection("users").document(tutorEMail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    User userData = doc.toObject(User.class);
                    tutorFullName = userData.getName() + ", " + userData.getFirstName();
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkForEmptyFields()) {
                    Exam exam = new Exam(etSubject.getText().toString(),
                            etAreaOfSubject.getText().toString(),
                            etStudentName.getText().toString() + ", " + etStudentFirstName.getText().toString(),
                            etStudentEmail.getText().toString(),
                            "In Abstimmung",
                            etSecondAssessorName.getText().toString(),
                            etSecondAssessorFirstName.getText().toString(),
                            "Rechnung noch nicht erstellt",
                            tutorEMail,
                            tutorFullName);
                    db.collection("Exams").add(exam);
                    Toast.makeText(CreateExamActivity.this, "Die zu betreuende Abschlussarbeit wurde eingetragen!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateExamActivity.this, TutorActivity.class));
                    finish();
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateExamActivity.this, TutorActivity.class));
                finish();
            }
        });
    }


    private boolean checkForEmptyFields() {
        if (etSubject.getText().toString().isEmpty() ||
                etAreaOfSubject.getText().toString().isEmpty() ||
                etStudentName.getText().toString().isEmpty() ||
                etStudentFirstName.getText().toString().isEmpty() ||
                etStudentEmail.getText().toString().isEmpty() ||
                etSecondAssessorName.getText().toString().isEmpty() ||
                etSecondAssessorFirstName.getText().toString().isEmpty()) {
            tvFailure.setText("Bitte füllen Sie alle Felder aus!");
            return true;
        } else {
            return false;
        }
    }

    private void initializeViewComponents() {
        etSubject = findViewById(R.id.et_create_exam_subject);
        etAreaOfSubject = findViewById(R.id.et_create_exam_area_of_subject);
        etStudentName = findViewById(R.id.et_create_exam_student_name);
        etStudentFirstName = findViewById(R.id.et_create_exam_student_first_name);
        etStudentEmail = findViewById(R.id.et_create_exam_student_email);
        etSecondAssessorName = findViewById(R.id.et_create_exam_second_assessor_name);
        etSecondAssessorFirstName = findViewById(R.id.et_create_exam_second_assessor_first_name);
        btnSubmit = findViewById(R.id.btn_create_exam_submit);
        btnCancel = findViewById(R.id.btn_create_exam_cancel);
        tvFailure = findViewById(R.id.tv_create_exam_failure);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(CreateExamActivity.this, MainActivity.class));
            Toast.makeText(this, "Sie wurden erfolgreich ausgeloggt!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else {
            return false;
        }
    }
}