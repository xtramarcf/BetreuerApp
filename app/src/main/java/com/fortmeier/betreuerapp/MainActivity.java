package com.fortmeier.betreuerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fortmeier.betreuerapp.tutor.TutorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {


    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        btnRegister = findViewById(R.id.register);

        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            finish();
        });

        btnLogin.setOnClickListener(view -> {
            loginUser(etEmail.getText().toString(), etPassword.getText().toString());
        });
    }

    private void loginUser(String email, String password) {
        if(!validateInputData()){
            Toast.makeText(this, "Bitte geben Sie Benutzernamen und Passwort ein!", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Anmeldung erfolgreich!", Toast.LENGTH_SHORT).show();
                redirectUser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Ungültige Benutzerdaten!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectUser() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userEmail);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String userType = doc.getData().get("userType").toString();
                    switch(userType){
                        case "Betreuer":
                            startActivity(new Intent(MainActivity.this, TutorActivity.class));
                            finish();
                            break;
                        case "Student":
                            startActivity(new Intent(MainActivity.this, StudentActivity.class));
                            finish();
                            break;
                        case "Zweitgutachter":
                            startActivity(new Intent(MainActivity.this, TutorActivity.class));
                            finish();
                            break;
                        default:
                            Toast.makeText(MainActivity.this, "Kein gültiger Benutzertyp", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Weiterleitung fehlgeschlagen.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            redirectUser();
        }

    }

    private boolean validateInputData(){
        if(etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
}