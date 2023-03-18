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
import com.fortmeier.betreuerapp.viewmodel.StudentViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StudentActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private StudentViewAdapter adapter;
    private Button btnTopics;
    private HashMap<String, User> userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        firebaseAuth = FirebaseAuth.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        btnTopics = findViewById(R.id.btn_topics);
        db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new StudentViewAdapter();
        recyclerView.setAdapter(adapter);

        userData = (HashMap<String, User>) getIntent().getSerializableExtra("map");

        adapter.setOnItemClickListener(new StudentViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User tutor) {
                Intent intent = new Intent(StudentActivity.this, ChatActivity.class);
                intent.putExtra("E-Mail", tutor.getEMail());
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });

        btnTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentActivity.this, TopicsActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });


        getAllTutors();

    }

    public void getAllTutors() {
        List<User> tutors = new ArrayList<>();
        db.collection("users").whereEqualTo("userType", "Betreuer")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            try {
                                tutors.add(new User(Objects.requireNonNull(doc.get("name")).toString(),
                                        Objects.requireNonNull(doc.get("firstName")).toString(),
                                        Objects.requireNonNull(doc.get("eMail")).toString(),
                                        Objects.requireNonNull(doc.get("expertises")).toString(),
                                        ""));
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                            }
                        }
                        adapter.setTutors(tutors);
                        adapter.submitList(tutors);
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
            startActivity(new Intent(StudentActivity.this, MainActivity.class));
            Toast.makeText(this, "Sie wurden erfolgreich ausgeloggt!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else {
            return false;
        }
    }

}