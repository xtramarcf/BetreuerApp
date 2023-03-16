package com.fortmeier.betreuerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.fortmeier.betreuerapp.model.Topic;
import com.fortmeier.betreuerapp.model.User;
import com.fortmeier.betreuerapp.tutor.QuestForDeleteActivity;
import com.fortmeier.betreuerapp.viewmodel.TopicViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TopicsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private TopicViewAdapter adapter;
    private String userEMail;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        userEMail = firebaseAuth.getCurrentUser().getEmail();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        adapter = new TopicViewAdapter();
        recyclerView.setAdapter(adapter);

        getAllTopics();

        adapter.setOnItemClickListener(new TopicViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Topic topic) {
                if (userType.equals("Betreuer")) {
                    Intent intent = new Intent(TopicsActivity.this, QuestForDeleteActivity.class);
                    intent.putExtra("Topic", topic);
                    startActivity(intent);
                } else if (userType.equals("Student")) {
                    Intent intent = new Intent(TopicsActivity.this, ChatActivity.class);
                    intent.putExtra("E-Mail", topic.getTutorEMail());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllTopics();
    }

    private void getAllTopics() {
        List<Topic> topics = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("users").document(userEMail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                userType = task.getResult().toObject(User.class).getUserType();
                if (userType.equals("Student")) {
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
        });
    }
}