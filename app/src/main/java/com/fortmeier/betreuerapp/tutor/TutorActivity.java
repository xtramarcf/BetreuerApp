package com.fortmeier.betreuerapp.tutor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fortmeier.betreuerapp.MainActivity;
import com.fortmeier.betreuerapp.R;
import com.fortmeier.betreuerapp.TopicsActivity;
import com.fortmeier.betreuerapp.model.Exam;
import com.fortmeier.betreuerapp.model.User;
import com.fortmeier.betreuerapp.viewmodel.TutorViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TutorActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private TutorViewAdapter adapter;
    private FloatingActionButton addExamTopic;
    private Button btnMessenger;
    private Button btnTopics;
    private String userType;
    private String userEmail;
    private String userName;
    private String userFirstName;
    private HashMap<String, User> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        addExamTopic = findViewById(R.id.fab_add_exam_topic);
        btnMessenger = findViewById(R.id.btn_messenger);
        btnTopics = findViewById(R.id.btn_topics);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userEmail = firebaseAuth.getCurrentUser().getEmail();

        userData = (HashMap<String, User>) getIntent().getSerializableExtra("map");
        userType = userData.get(userEmail).getUserType();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        adapter = new TutorViewAdapter();
        recyclerView.setAdapter(adapter);



        if (userType.equals("Zweitgutachter")) {
            addExamTopic.setVisibility(View.GONE);
            btnTopics.setVisibility(View.GONE);
        } else {
            adapter.setOnItemClickListener(new TutorViewAdapter.OnItemCLickListener() {
                @Override
                public void onItemClick(Exam exam) {
                    if(exam.getTutorFullName().equals(userName+", "+userFirstName)){
                        Intent intent = new Intent(TutorActivity.this, EditExamActivity.class);
                        intent.putExtra("Exam", exam);
                        intent.putExtra("map",userData);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }




        addExamTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorActivity.this, QuestTutorAddingActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });

        btnTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorActivity.this, TopicsActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });

        btnMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorActivity.this, ChatOverviewActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });


        getAllExams();
    }


    public void getAllExams() {
        List<Exam> exams = new ArrayList<>();


        userName = userData.get(userEmail).getName();
        userFirstName = userData.get(userEmail).getFirstName();

        db.collection("Exams").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                try {
                                    Exam exam = new Exam(doc.get("theme").toString(),
                                            doc.get("subject").toString(),
                                            doc.get("studentName").toString(),
                                            doc.get("studentEMail").toString(),
                                            doc.get("status").toString(),
                                            doc.get("secondAssessorName").toString(),
                                            doc.get("secondAssessorFirstName").toString(),
                                            doc.get("billStatus").toString(),
                                            doc.get("tutorEMail").toString(),
                                            doc.get("tutorFullName").toString());
                                    exam.setId(doc.getId());
                                    String fullUserName = userName + ", " + userFirstName;

                                    if (exam.getSecondAssessorName().equals(userName) && exam.getSecondAssessorFirstName().equals(userFirstName)
                                            || exam.getTutorFullName().equals(fullUserName)) {
                                        exams.add(exam);
                                    }
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            adapter.setExams(exams);
                            adapter.submitList(exams);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout || item.getItemId() == R.id.back_arrow) {
            firebaseAuth.signOut();
            startActivity(new Intent(TutorActivity.this, MainActivity.class));
            Toast.makeText(this, "Sie wurden erfolgreich ausgeloggt!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else {
            return false;
        }
    }

}