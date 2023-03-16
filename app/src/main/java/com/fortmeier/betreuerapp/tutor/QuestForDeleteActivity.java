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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class QuestForDeleteActivity extends AppCompatActivity {

    private Button btnDelete;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_for_delete);

        btnDelete = findViewById(R.id.btn_delete);
        btnBack = findViewById(R.id.btn_back);

        Topic topic = (Topic) getIntent().getSerializableExtra("Topic");

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("OfferedTopics").whereEqualTo("subject", topic.getSubject()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(!value.isEmpty()){
                            String docId = value.getDocuments().get(0).getId();
                            FirebaseFirestore.getInstance().collection("OfferedTopics").document(docId).delete();
                            startActivity(new Intent(QuestForDeleteActivity.this, TutorActivity.class));
                            Toast.makeText(QuestForDeleteActivity.this, "Ausgeschriebens Thema wurde gelöscht!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuestForDeleteActivity.this, TutorActivity.class));
                finish();
            }
        });
    }
}