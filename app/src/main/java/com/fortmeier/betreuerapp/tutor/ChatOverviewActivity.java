package com.fortmeier.betreuerapp.tutor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fortmeier.betreuerapp.ChatActivity;
import com.fortmeier.betreuerapp.MainActivity;
import com.fortmeier.betreuerapp.R;
import com.fortmeier.betreuerapp.model.User;
import com.fortmeier.betreuerapp.viewmodel.ChatsOverviewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatOverviewActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ChatsOverviewAdapter adapter;
    private String userEmail;
    private EditText etSearchWithEmail;
    private Button btnSearch;
    private TextView tvFailure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_overview);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        etSearchWithEmail = findViewById(R.id.et_search_contact);
        btnSearch = findViewById(R.id.btn_search_contact);
        tvFailure = findViewById(R.id.tv_search_contact_failure);

        userEmail = auth.getCurrentUser().getEmail();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();

        adapter = new ChatsOverviewAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(user -> {
            Intent intent = new Intent(ChatOverviewActivity.this, ChatActivity.class);
            intent.putExtra("E-Mail", user.getEMail());
            startActivity(intent);
        });
        getAllUserChats();

        btnSearch.setOnClickListener(view -> {
            if (!etSearchWithEmail.getText().toString().isEmpty())
                db.collection("users").document(etSearchWithEmail.getText().toString()).addSnapshotListener((value, error) -> {
                    if (value.exists()) {
                        tvFailure.setText("");
                        Intent intent = new Intent(ChatOverviewActivity.this, ChatActivity.class);
                        intent.putExtra("E-Mail", etSearchWithEmail.getText().toString());
                        startActivity(intent);
                    } else {
                        tvFailure.setText("Kontakt nicht gefunden!");
                    }
                });
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllUserChats();
    }

    private void getAllUserChats() {
        String userName;
        String userFirstName;
        List<User> userChats = new ArrayList<>();
        db.collection("messages").document(userEmail).collection("contacts").orderBy("timeStampMilli", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            List<User> chatContactList = task.getResult().toObjects(User.class);
            for (User contact : chatContactList) {
                User user = new User();
                user.setEMail(contact.getEMail());
                user.setLastMessage(contact.getLastMessage());
                user.setTimeStamp(contact.getTimeStamp());
                db.collection("users").document(user.getEMail()).addSnapshotListener((value, error) -> {
                    User userdata = value.toObject(User.class);
                    user.setName(userdata.getName());
                    user.setFirstName(userdata.getFirstName());
                    userChats.add(user);
                    adapter.setUsers(userChats);
                    adapter.submitList(userChats);
                });
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
            auth = FirebaseAuth.getInstance();
            auth.signOut();
            startActivity(new Intent(ChatOverviewActivity.this, MainActivity.class));
            Toast.makeText(this, "Sie wurden erfolgreich ausgeloggt!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else {
            return false;
        }
    }
}



