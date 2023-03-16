package com.fortmeier.betreuerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fortmeier.betreuerapp.model.ChatMessage;
import com.fortmeier.betreuerapp.model.Topic;
import com.fortmeier.betreuerapp.model.User;
import com.fortmeier.betreuerapp.viewmodel.ChatViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ChatViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<ChatMessage> list;
    private TextInputLayout message;
    private FloatingActionButton send;
    private TextView tvContactName;
    private FirebaseAuth auth;
    private FirebaseUser fbUser;
    private FirebaseFirestore fdb;
    private DatabaseReference db;
    private String userEMail;
    private String contactEMail;

    public static final String MESSAGES = "messages";
    public static final String CONTACTS = "contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        send = findViewById(R.id.fab_chat_send);
        message = findViewById(R.id.til_chat_message);
        tvContactName = findViewById(R.id.tv_contact_name);
        recyclerView = findViewById(R.id.recycler_view_chat);
        list = new ArrayList<>();

        db = FirebaseDatabase.getInstance().getReference();
        fdb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        fbUser = auth.getCurrentUser();
        userEMail = fbUser.getEmail();
        contactEMail = (String) getIntent().getSerializableExtra("E-Mail");

        fdb.collection("users").document(contactEMail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                tvContactName.setText(user.getName() + ", " + user.getFirstName());
            }
        });


        adapter = new ChatViewAdapter(this, list);
        LinearLayoutManager llm = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = message.getEditText().getText().toString();
                message.getEditText().setText("");
                String timeStamp = new SimpleDateFormat("dd-MM-yy HH:mm a").format(Calendar.getInstance().getTime());

                ChatMessage chatMessage = new ChatMessage(userEMail, msg, timeStamp, Timestamp.now().toString());
                addContactData(msg, timeStamp);
                fdb.collection(MESSAGES).document(userEMail).collection(MESSAGES).document(contactEMail).collection(MESSAGES).add(chatMessage).
                        addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                fdb.collection(MESSAGES).document(contactEMail).collection(MESSAGES).document(userEMail).collection(MESSAGES).add(chatMessage);
                            }
                        });
            }
        });

    }

    private void addContactData(String message, String timeStamp) {

        Date date = new Date();
        Long timeStampLong = date.getTime();
        String timeStampString = timeStampLong.toString();

        fdb.collection("messages").document(userEMail).collection(CONTACTS).whereEqualTo("eMail", contactEMail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()) {
                    Map<String, Object> contactMap = new ArrayMap<>();
                    contactMap.put("eMail", contactEMail);
                    contactMap.put("lastMessage", userEMail + ": \"" + message + "\"");
                    contactMap.put("timeStamp", timeStamp);
                    contactMap.put("timeStampMilli", timeStampString);
                    fdb.collection(MESSAGES).document(userEMail).collection(CONTACTS).add(contactMap);
                } else {
                    fdb.collection(MESSAGES).document(userEMail).collection(CONTACTS).document(value.getDocuments().get(0).getId())
                            .update("lastMessage", userEMail + ": " + message
                                    , "timeStamp", timeStamp,
                                    "timeStampMilli", timeStampString);
                }
            }
        });
        fdb.collection(MESSAGES).document(contactEMail).collection(CONTACTS).whereEqualTo("eMail", userEMail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()) {
                    Map<String, Object> userMap = new ArrayMap<>();
                    userMap.put("eMail", userEMail);
                    userMap.put("lastMessage", userEMail + ": \"" + message + "\"");
                    userMap.put("timeStamp", timeStamp);
                    userMap.put("timeStampMilli", timeStampString);
                    fdb.collection(MESSAGES).document(contactEMail).collection(CONTACTS).add(userMap);
                } else {
                    fdb.collection(MESSAGES).document(contactEMail).collection(CONTACTS).document(value.getDocuments().get(0).getId())
                            .update("lastMessage", userEMail + ": " + message
                                    , "timeStamp", timeStamp,
                                    "timeStampMilli", timeStampString);
                }
            }
        });

    }

    private void receiveMessages() {
        fdb.collection(MESSAGES).document(userEMail).collection(MESSAGES).document(contactEMail).collection(MESSAGES).orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.err.println("Failed to receive data: " + error);
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    ChatMessage chatMessage = dc.getDocument().toObject(ChatMessage.class);
                    adapter.addMessage(chatMessage);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        receiveMessages();
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
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
            Toast.makeText(this, "Sie wurden erfolgreich ausgeloggt!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else {
            return false;
        }
    }


}

