package com.fortmeier.betreuerapp;

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

import com.fortmeier.betreuerapp.model.User;
import com.fortmeier.betreuerapp.tutor.EditExamActivity;
import com.fortmeier.betreuerapp.viewmodel.StudentViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private StudentViewAdapter adapter;
    private Button btnTopics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        firebaseAuth = FirebaseAuth.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        btnTopics = findViewById(R.id.btn_topics);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new StudentViewAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new StudentViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User tutor) {
                Intent intent = new Intent(StudentActivity.this, ChatActivity.class);
                intent.putExtra("E-Mail", tutor.getEMail());
                startActivity(intent);
            }
        });

        btnTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentActivity.this, TopicsActivity.class));
                Toast.makeText(StudentActivity.this, "Drücken um Tutor zu kontaktieren!", Toast.LENGTH_SHORT).show();
            }
        });


        getAllTutors();
        Toast.makeText(this, "Zum kontaktieren drücken!", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllTutors();
    }

    public void getAllTutors() {
        List<User> tutors = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("users").whereEqualTo("userType", "Betreuer")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                try {
                                    tutors.add(new User(doc.get("name").toString(), doc.get("firstName")
                                            .toString(), doc.get("eMail").toString(), doc.get("expertises").toString(), ""));
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            adapter.setTutors(tutors);
                            adapter.submitList(tutors);
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
        if (item.getItemId() == R.id.logout) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(StudentActivity.this, MainActivity.class));
            Toast.makeText(this, "Sie wurden erfolgreich ausgeloggt!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else {
            return false;
        }
    }

}