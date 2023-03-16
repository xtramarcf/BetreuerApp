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
import android.widget.Toast;

import com.fortmeier.betreuerapp.MainActivity;
import com.fortmeier.betreuerapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class QuestTutorAddingActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button btnOfferTopic;
    private Button btnCreateExam;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_tutor_adding);
        btnOfferTopic = findViewById(R.id.btn_post_topic);
        btnCreateExam = findViewById(R.id.btn_create_exam);
        btnBack = findViewById(R.id.btn_back);

        btnOfferTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuestTutorAddingActivity.this, OfferTopicActivity.class));
                finish();
            }
        });

        btnCreateExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuestTutorAddingActivity.this, CreateExamActivity.class));
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuestTutorAddingActivity.this, TutorActivity.class));
                finish();
            }
        });

    }
}