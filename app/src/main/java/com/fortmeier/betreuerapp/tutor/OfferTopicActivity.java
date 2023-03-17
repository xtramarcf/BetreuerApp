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
import com.fortmeier.betreuerapp.model.Topic;
import com.fortmeier.betreuerapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class OfferTopicActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private EditText etSubject;
    private EditText etAreaOfSubject;
    private EditText etSubjectDescription;
    private Button btnSubmit;
    private Button btnCancel;
    private TextView tvFailure;
    private String tutorFullName;
    private String tutorEMail;
    private HashMap<String, User> userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_topic);
        etSubject = findViewById(R.id.et_offer_topic_subject);
        etAreaOfSubject = findViewById(R.id.et_offer_topic_area_of_subject);
        etSubjectDescription = findViewById(R.id.et_offer_topic_subject_description);
        btnSubmit = findViewById(R.id.btn_offer_topic_submit);
        btnCancel = findViewById(R.id.btn_offer_topic_cancel);
        tvFailure = findViewById(R.id.tv_offer_topic_failure);
        userData = (HashMap<String, User>) getIntent().getSerializableExtra("map");
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();
        tutorEMail = user.getEmail();


        tutorFullName = userData.get(tutorEMail).getName() + ", " + userData.get(tutorEMail).getFirstName();



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etSubject.getText().toString().isEmpty() ||
                        etAreaOfSubject.getText().toString().isEmpty() ||
                        etSubjectDescription.getText().toString().isEmpty()){
                    tvFailure.setText("Bitte füllen Sie alle Felder aus!");
                }
                else{
                    Topic topic = new Topic(etAreaOfSubject.getText().toString(),
                            etSubject.getText().toString(),
                            etSubjectDescription.getText().toString(),
                            tutorEMail,
                            tutorFullName);
                    db.collection("OfferedTopics").add(topic);
                    Toast.makeText(OfferTopicActivity.this, "Das Thema für die Abschlussarbeit ist ausgeschrieben!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OfferTopicActivity.this, TutorActivity.class);
                    intent.putExtra("map", userData);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OfferTopicActivity.this, TutorActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });


    }
}