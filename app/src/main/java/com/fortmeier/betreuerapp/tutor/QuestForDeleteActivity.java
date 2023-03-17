package com.fortmeier.betreuerapp.tutor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fortmeier.betreuerapp.R;
import com.fortmeier.betreuerapp.TopicsActivity;
import com.fortmeier.betreuerapp.model.Topic;
import com.fortmeier.betreuerapp.model.User;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class QuestForDeleteActivity extends AppCompatActivity {

    private Button btnDelete;
    private Button btnBack;
    private HashMap<String, User> userData;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_for_delete);

        btnDelete = findViewById(R.id.btn_delete);
        btnBack = findViewById(R.id.btn_back);

        db = FirebaseFirestore.getInstance();

        userData = (HashMap<String, User>) getIntent().getSerializableExtra("map");
        Topic topic = (Topic) getIntent().getSerializableExtra("Topic");

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("OfferedTopics").whereEqualTo("subject", topic.getSubject()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(!value.isEmpty()){
                            String docId = value.getDocuments().get(0).getId();
                            db.collection("OfferedTopics").document(docId).delete();
                            Toast.makeText(QuestForDeleteActivity.this, "Ausgeschriebens Thema wurde gelöscht!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(QuestForDeleteActivity.this, TutorActivity.class);
                            intent.putExtra("map", userData);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestForDeleteActivity.this, TutorActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });
    }
}