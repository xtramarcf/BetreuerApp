package com.fortmeier.betreuerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fortmeier.betreuerapp.model.Topic;
import com.fortmeier.betreuerapp.model.User;
import com.fortmeier.betreuerapp.tutor.ChatOverviewActivity;
import com.fortmeier.betreuerapp.tutor.QuestForDeleteActivity;
import com.fortmeier.betreuerapp.tutor.TutorActivity;
import com.fortmeier.betreuerapp.viewmodel.TopicViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopicsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private TopicViewAdapter adapter;
    private String userEMail;
    private String userType;
    private RecyclerView recyclerView;
    private HashMap<String, User> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        userEMail = firebaseAuth.getCurrentUser().getEmail();

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        adapter = new TopicViewAdapter();
        recyclerView.setAdapter(adapter);

        userData = (HashMap<String, User>) getIntent().getSerializableExtra("map");
        userType = userData.get(userEMail).getUserType();

        getAllTopics();

        adapter.setOnItemClickListener(new TopicViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Topic topic) {
                if (userType.equals("Betreuer")) {
                    Intent intent = new Intent(TopicsActivity.this, QuestForDeleteActivity.class);
                    intent.putExtra("Topic", topic);
                    intent.putExtra("map", userData);
                    startActivity(intent);
                    finish();
                } else if (userType.equals("Student")) {
                    Intent intent = new Intent(TopicsActivity.this, ChatActivity.class);
                    intent.putExtra("E-Mail", topic.getTutorEMail());
                    intent.putExtra("map", userData);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void getAllTopics() {
        List<Topic> topics = new ArrayList<>();

        if (userData.get(userEMail).getUserType().equals("Student")) {
            db.collection("OfferedTopics").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Topic topic = doc.toObject(Topic.class);
                        topics.add(topic);
                    }
                    adapter.setTopics(topics);
                    adapter.submitList(topics);
                }
            });
        } else {
            db.collection("OfferedTopics").whereEqualTo("tutorEMail", userEMail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Topic topic = doc.toObject(Topic.class);
                        topics.add(topic);
                    }
                    adapter.setTopics(topics);
                    adapter.submitList(topics);
                }
            });
        }
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
            firebaseAuth.signOut();
            startActivity(new Intent(TopicsActivity.this, MainActivity.class));
            Toast.makeText(this, "Sie wurden erfolgreich ausgeloggt!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else if (item.getItemId() == R.id.back_arrow) {
            if (userData.get(userEMail).getUserType().equals("Student")) {
                Intent intent = new Intent(TopicsActivity.this, StudentActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(TopicsActivity.this, TutorActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
            return true;
        } else {
            return false;
        }
    }
}